package com.hoteltransilvania.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

            // Desactivar CSRF
            .csrf(csrf -> csrf.disable())

            // Configurar permisos
            .authorizeHttpRequests(auth -> auth

                    // Permitir login sin autenticación
                    .requestMatchers(
                            "/usuarios/login",
                            "/usuarios/logeados"
                    ).permitAll()

                    // Permitir swagger si usas swagger
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll()

                    // Todo lo demás requiere autenticación
                    .anyRequest().authenticated()
            )

            // Desactivar formulario login de Spring
            .formLogin(form -> form.disable())

            // Desactivar basic auth
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}