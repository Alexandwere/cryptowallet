package com.javaacademy.cryptowallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("API сервиса Криптокошельков")
                .description("""
                    API для управления криптосчетами и пользователями.
                    Регистрация и управление пользователями.
                    Открытие счетов и проведение по ним операций.""")
                .contact(new Contact().name("Alexandwere").url("https://t.me/Alexandwere"));
        return new OpenAPI().info(info);
    }

}
