package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EsmApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(EsmApplication.class, args);
    }
}
