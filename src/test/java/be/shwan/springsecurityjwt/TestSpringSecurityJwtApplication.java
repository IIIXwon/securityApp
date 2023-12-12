package be.shwan.springsecurityjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringSecurityJwtApplication::main).with(TestSpringSecurityJwtApplication.class).run(args);
    }

}
