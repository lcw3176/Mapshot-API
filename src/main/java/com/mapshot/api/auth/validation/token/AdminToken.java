package com.mapshot.api.auth.validation.token;

import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Date;

@Component
public class AdminToken {

    @Value("${jwt.admin.secret}")
    private String JWT_SECRET;

    @Value("${jwt.admin.second}")
    private int DEFAULT_SECONDS;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;

    public String makeToken() {
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(DEFAULT_SECONDS).toMillis()))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public MultiValueMap<String, String> getTokenHeader() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(ADMIN_HEADER_NAME, makeToken());

        return map;
    }
    
    public void isValid(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);

        } catch (Exception e) {
            throw new ApiException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

}
