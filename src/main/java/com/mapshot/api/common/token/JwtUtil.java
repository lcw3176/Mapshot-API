package com.mapshot.api.common.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.Date;

@UtilityClass
public class JwtUtil {

    private static final String JWT_IMAGE_SECRET = System.getenv("jwt_image");
    private static final String JWT_ADMIN_SECRET = System.getenv("jwt_admin");
    private static final int DEFAULT_IMAGE_SECOND = 60;
    private static final int DEFAULT_ADMIN_SECONDS = 60 * 60;
    public static final String HEADER_NAME = "AUTH_TOKEN";
    public static final String ADMIN_HEADER_NAME = "ADMIN_AUTH_TOKEN";

    public String generate() {
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(DEFAULT_IMAGE_SECOND).toMillis()))
                .signWith(SignatureAlgorithm.HS512, JWT_IMAGE_SECRET)
                .compact();
    }

    public String generateAdmin() {
        Date now = new Date();
        
        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(DEFAULT_ADMIN_SECONDS).toMillis()))
                .signWith(SignatureAlgorithm.HS512, JWT_ADMIN_SECRET)
                .compact();
    }


    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_IMAGE_SECRET).parseClaimsJws(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }


    public boolean isValidAdmin(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_ADMIN_SECRET).parseClaimsJws(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

}
