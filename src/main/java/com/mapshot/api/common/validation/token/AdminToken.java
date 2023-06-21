package com.mapshot.api.common.validation.token;

import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class AdminToken {

    @Value("${jwt.admin.secret}")
    private String JWT_SECRET;

    @Value("${jwt.admin.second}")
    private int DEFAULT_SECONDS;

    @Value("${jwt.admin.header}")
    public String HEADER_NAME;

    public String generate() {
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(DEFAULT_SECONDS).toMillis()))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getHeaderName() {
        return this.HEADER_NAME;
    }

    public void isValid(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);

        } catch (Exception e) {
            throw new ApiException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

}
