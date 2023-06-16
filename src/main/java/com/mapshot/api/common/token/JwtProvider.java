package com.mapshot.api.common.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;

public class JwtProvider {

    private JwtProvider() {

    }

    private static final String JWT_SECRET = System.getenv("JWT_SECRET");
    private static final int DEFAULT_SECOND = 60;
    public static final String HEADER_NAME = "AUTH_TOKEN";


    public static String generate() {
        return makeToken(DEFAULT_SECOND);
    }

    public static String generate(int seconds) {
        return makeToken(seconds);
    }

    private static String makeToken(int seconds){
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(seconds).toMillis()))
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
