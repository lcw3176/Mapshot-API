package com.mapshot.api.presentation.community.post.model;

import com.mapshot.api.domain.community.post.PostConst;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRegisterRequest {

    @NotBlank
    private String writer;

    @NotBlank
    @Length(max = PostConst.MAX_TITLE_LENGTH)
    private String title;

    @NotBlank
    @Length(max = PostConst.MAX_CONTENT_LENGTH)
    private String content;

    @NotBlank
    private String password;

}
