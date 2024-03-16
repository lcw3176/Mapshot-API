package com.mapshot.api.auth.validation;

import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    public MultiValueMap<String, String> getTokenHeader(String headerName, String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(headerName, token);

        return map;
    }

    public void isValid(String secretKey, String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

        } catch (Exception e) {
            throw new ApiException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

}
