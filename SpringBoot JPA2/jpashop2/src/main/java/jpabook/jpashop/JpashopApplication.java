package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashopApplication.class, args);

    }
    //기본적으로 초기화된 프록시 객체만 노출, 초기화되지않는 프록시 객체는 노출안함.
    @Bean
    Hibernate5JakartaModule hibernate5Module() {
        return new Hibernate5JakartaModule();
    }

}
