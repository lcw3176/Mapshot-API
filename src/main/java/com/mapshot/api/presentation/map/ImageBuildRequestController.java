package com.mapshot.api.presentation.map;

import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/image/generate")
@RequiredArgsConstructor
public class ImageBuildRequestController {

    private final Validation serverValidation;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;

    @Value("${client.lambda.url}")
    private String lambdaUrl;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public void redirectToLambda(@ModelAttribute MapBuildRequest mapRequest, HttpServletResponse httpServletResponse) throws IOException {
        MultiValueMap<String, String> queries = mapRequest.toQueryParams();
        queries.add(SERVER_HEADER_NAME, serverValidation.makeToken());

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(lambdaUrl)
                .queryParams(queries)
                .build()
                .toString();

        httpServletResponse.sendRedirect(url);
    }
}
