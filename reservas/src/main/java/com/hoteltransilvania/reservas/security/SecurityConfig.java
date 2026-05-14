package com.hoteltransilvania.reservas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(
            JwtFilter jwtFilter
    ) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // DESACTIVAR CSRF
                .csrf(csrf -> csrf.disable())

                // CONFIGURAR RUTAS
                .authorizeHttpRequests(auth -> auth

                        // TODO requiere token
                        .anyRequest().authenticated()
                )

                // DESACTIVAR LOGIN WEB
                .formLogin(form -> form.disable())

                // DESACTIVAR BASIC AUTH
                .httpBasic(httpBasic -> httpBasic.disable())

                // ACTIVAR JWT FILTER
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}