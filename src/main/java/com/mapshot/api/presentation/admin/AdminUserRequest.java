package com.mapshot.api.presentation.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserRequest {
    private String nickname;
    private String password;
}
