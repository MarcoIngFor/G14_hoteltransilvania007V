package com.hoteltransilvania.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.hoteltransilvania.usuarios.security.JwtFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Desactiva CSRF para pruebas con Postman
                .csrf(csrf -> csrf.disable())

                // Permisos por ruta
                .authorizeHttpRequests(auth -> auth

                        // Login libre
                        .requestMatchers("/usuarios/login").permitAll()

                        // Ver logeados libre por ahora
                        .requestMatchers("/usuarios/logeados").permitAll()

                        // Todo lo demás necesita token
                        .anyRequest().authenticated()
                )

                // Desactiva login web
                .formLogin(form -> form.disable())

                // Desactiva Basic Auth
                .httpBasic(httpBasic -> httpBasic.disable())

                // Activa el filtro JWT
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}