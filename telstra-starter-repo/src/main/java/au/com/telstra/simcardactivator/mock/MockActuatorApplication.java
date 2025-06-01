package au.com.telstra.simcardactivator.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "au.com.telstra.simcardactivator.mock")
public class MockActuatorApplication {
    public static void main(String[] args) {
        System.setProperty("server.port", "8444");
        SpringApplication.run(MockActuatorApplication.class, args);
    }
}
