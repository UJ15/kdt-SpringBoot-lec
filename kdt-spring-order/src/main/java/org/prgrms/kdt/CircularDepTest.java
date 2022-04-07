package org.prgrms.kdt;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class A{
    B b;
    A(B b){
        this.b = b;
    }
}

class B{
    A a;
    B(A a){
        this.a = a;
    }
}
@Configuration // 실행하면 얘까지 스캔해버린다.
class CircularConfig {
    @Bean
    public A a(B b){
        return new A(b);
    }

    @Bean
    public B b(A a){
        return new B(a);
    }

}

public class CircularDepTest {
    public static void main(String[] args) {
        var annotationConfigApplicationContext =  new AnnotationConfigApplicationContext(CircularConfig.class);
    }

}
