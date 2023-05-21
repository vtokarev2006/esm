package com.epam.esm.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@Profile("dev")
public class DevelopmentConfiguration {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}