package com.hackorama.flags.server.spring;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring Boot application
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
@SpringBootApplication
public class Application {

    private static ConfigurableApplicationContext context;

    public static void start(int port) {
        String[] args = {};
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", port));
        context = app.run(args);
    }

    public static void stop() {
        if (context != null) {
            context.close();
        }
    }

}
