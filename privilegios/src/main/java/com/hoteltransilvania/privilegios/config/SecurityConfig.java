package com.hoteltransilvania.privilegios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Permitir endpoints del microservicio
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Desactivar CSRF
                .csrf(csrf -> csrf.disable())

                // Permitir acceso libre
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/privilegios/**").permitAll()
                        .anyRequest().permitAll()
                )

                // Desactivar login web
                .formLogin(form -> form.disable())

                // Desactivar basic auth
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}