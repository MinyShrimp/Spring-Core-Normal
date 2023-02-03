package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class StatefulServiceTest {
    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }

        @Bean
        public StatelessService statelessService() {
            return new StatelessService();
        }
    }

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService ss1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService ss2 = ac.getBean("statefulService", StatefulService.class);

        // ThreadA: A 사용자가 10000원 주문
        ss1.order("userA", 10000);

        // ThreadB: B 사용자가 20000원 주문
        ss2.order("userB", 20000);

        // ThreadA: 사용자A 주문 금액 조회
        int price = ss1.getPrice();
        // ThreadA: 사용자A는 10000원을 기대했지만, 기대와 다르게 20000원 출력
        System.out.println("price = " + price);

        Assertions.assertThat(ss1.getPrice()).isNotEqualTo(10000);
    }

    @Test
    void statelessServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatelessService ss1 = ac.getBean("statelessService", StatelessService.class);
        StatelessService ss2 = ac.getBean("statelessService", StatelessService.class);

        // ThreadA: A 사용자가 10000원 주문
        int userAPrice = ss1.order("userA", 10000);

        // ThreadB: B 사용자가 20000원 주문
        int userBPrice = ss2.order("userB", 20000);

        // ThreadA: 사용자A 주문 금액 조회
        // ThreadA: 사용자A는 10000원을 기대했지만, 기대와 다르게 20000원 출력
        System.out.println("price = " + userAPrice);
        Assertions.assertThat(userAPrice).isEqualTo(10000);
        Assertions.assertThat(userBPrice).isEqualTo(20000);
    }
}
