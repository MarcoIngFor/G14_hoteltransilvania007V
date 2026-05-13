package com.hoteltransilvania.roles.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Permite usar los endpoints desde Postman y otros microservicios
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Desactiva CSRF
                .csrf(csrf -> csrf.disable())

                // Permite todos los endpoints de roles
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/roles/**").permitAll()
                        .anyRequest().permitAll()
                )

                // Desactiva login por formulario de Spring
                .formLogin(form -> form.disable())

                // Desactiva Basic Auth
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}