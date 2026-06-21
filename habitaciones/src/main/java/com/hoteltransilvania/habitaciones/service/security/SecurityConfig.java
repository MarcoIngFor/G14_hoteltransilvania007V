package com.hoteltransilvania.habitaciones.service.security;

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

                        // Permite consultas internas desde reservas
                        .requestMatchers(HttpMethod.GET, "/habitaciones/**").hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // Crear habitación
                        .requestMatchers(HttpMethod.POST, "/habitaciones/**").hasRole("ADMIN")

                        // Modificar habitación o estado
                        .requestMatchers(HttpMethod.PUT, "/habitaciones/**").hasRole("ADMIN")

                        // Eliminar habitación
                        .requestMatchers(HttpMethod.DELETE, "/habitaciones/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}