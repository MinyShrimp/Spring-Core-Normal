package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class PrototypeTest {
    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void clear() {
            System.out.println("PrototypeBean.clear");
        }
    }

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototype bean 1");
        PrototypeBean pb1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototype bean 2");
        PrototypeBean pb2 = ac.getBean(PrototypeBean.class);

        System.out.println("prototype bean 1 = " + pb1);
        System.out.println("prototype bean 2 = " + pb2);

        Assertions.assertThat(pb1).isNotSameAs(pb2);

        ac.close();
    }
}
