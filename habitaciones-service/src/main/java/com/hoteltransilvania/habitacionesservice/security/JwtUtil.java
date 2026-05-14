package com.hoteltransilvania.habitacionesservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY =
            "hoteltransilvaniahoteltransilvania123456";

    private final long EXPIRATION =
            1000 * 60 * 60;

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }

    // GENERAR TOKEN
    public String generarToken(
            String username,
            Long rolId,
            Object privilegios
    ) {

        return Jwts.builder()

                .setSubject(username)

                .claim("rolId", rolId)

                .claim(
                        "privilegios",
                        privilegios
                )

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION
                        )
                )

                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // EXTRAER USERNAME
    public String extraerUsername(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(
                        getSigningKey()
                )

                .build()

                .parseClaimsJws(token)

                .getBody()

                .getSubject();
    }

    // EXTRAER PRIVILEGIOS
    public Object extraerPrivilegios(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(
                        getSigningKey()
                )

                .build()

                .parseClaimsJws(token)

                .getBody()

                .get("privilegios");
    }

    // VALIDAR TOKEN
    public boolean validarToken(
            String token
    ) {

        try {

            Jwts.parserBuilder()

                    .setSigningKey(
                            getSigningKey()
                    )

                    .build()

                    .parseClaimsJws(token);

            return true;

        } catch (JwtException e) {

            return false;
        }
    }
}