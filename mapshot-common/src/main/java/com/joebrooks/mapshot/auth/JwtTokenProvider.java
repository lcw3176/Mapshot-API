package com.joebrooks.mapshot.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtTokenProvider {

    // fixme 나중에 키 값 뺄것
    private static final String JWT_SECRET = "iamtestkey";
    public static final String HEADER_NAME = "JWT_TOKEN";

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
