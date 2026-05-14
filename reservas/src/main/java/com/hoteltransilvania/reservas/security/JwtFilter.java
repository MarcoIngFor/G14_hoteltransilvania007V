package com.hoteltransilvania.reservas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(
            JwtUtil jwtUtil
    ) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // OBTENER HEADER AUTHORIZATION
        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        // VALIDAR BEARER TOKEN
        if (
                authHeader != null &&
                authHeader.startsWith("Bearer ")
        ) {

            // EXTRAER TOKEN
            String token =
                    authHeader.substring(7);

            // VALIDAR TOKEN
            if (jwtUtil.validarToken(token)) {

                // EXTRAER USERNAME
                String username =
                        jwtUtil.extraerUsername(token);

                // EXTRAER PRIVILEGIOS
                Object privilegiosObj =
                        jwtUtil.extraerPrivilegios(token);

                List<SimpleGrantedAuthority> authorities =
                        new ArrayList<>();

                // CONVERTIR PRIVILEGIOS
                if (
                        privilegiosObj instanceof List<?> lista
                ) {

                    for (Object item : lista) {

                        if (
                                item instanceof Map<?, ?> privilegio
                        ) {

                            Object nombre =
                                    privilegio.get("nombre");

                            if (nombre != null) {

                                authorities.add(
                                        new SimpleGrantedAuthority(
                                                nombre.toString()
                                        )
                                );
                            }
                        }
                    }
                }

                // CREAR AUTENTICACIÓN
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                // GUARDAR AUTENTICACIÓN
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}