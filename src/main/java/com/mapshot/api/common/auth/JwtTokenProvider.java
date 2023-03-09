package com.mapshot.api.common.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;

public class JwtTokenProvider {

    private JwtTokenProvider() {

    }

    private static final String JWT_SECRET = System.getenv("JWT_SECRET");
    public static final String HEADER_NAME = "AUTH_TOKEN";

    public static String generate() {
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(60).toMillis()))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    public static boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

}
