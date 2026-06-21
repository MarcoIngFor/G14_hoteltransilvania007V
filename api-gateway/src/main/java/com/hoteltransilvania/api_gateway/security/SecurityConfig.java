package com.hoteltransilvania.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/usuarios/login").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/clientes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.GET, "/habitaciones/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.GET, "/reservas/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.GET, "/servicioextra/**", "/tiposervicio/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "CAJERO")

                        .requestMatchers(HttpMethod.GET, "/consumo/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "CAJERO")

                        .requestMatchers(HttpMethod.GET, "/pagos/**")
                        .hasAnyRole("ADMIN", "CAJERO")

                        .requestMatchers(HttpMethod.GET, "/reportes/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/notificaciones/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/reservas/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/consumo/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/pagos/**")
                        .hasAnyRole("ADMIN", "CAJERO")

                        .requestMatchers(HttpMethod.POST, "/notificaciones/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}