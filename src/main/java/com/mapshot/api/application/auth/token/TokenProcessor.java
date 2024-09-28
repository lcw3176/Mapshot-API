package com.mapshot.api.application.auth.token;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class TokenProcessor {

    public String makeToken(int defaultSeconds, String secretKey) {
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(defaultSeconds).toMillis()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public void isValid(String secretKey, String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

        } catch (Exception e) {
            throw new ApiException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

}
