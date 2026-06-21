package com.hoteltransilvania.consumo.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll()

                        // SERVICIOS EXTRA
                        .requestMatchers(HttpMethod.GET, "/servicioextra/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "CAJERO")

                        .requestMatchers(HttpMethod.POST, "/servicioextra/**")
                        .hasRole("ADMIN")

                        // TIPOS DE SERVICIO
                        .requestMatchers(HttpMethod.GET, "/tiposervicio/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "CAJERO")

                        .requestMatchers(HttpMethod.POST, "/tiposervicio/**")
                        .hasRole("ADMIN")

                        // CONSUMOS
                        .requestMatchers(HttpMethod.GET, "/consumo/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "CAJERO")

                        .requestMatchers(HttpMethod.POST, "/consumo/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // FUTUROS PUT/DELETE
                        .requestMatchers(HttpMethod.PUT, "/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}