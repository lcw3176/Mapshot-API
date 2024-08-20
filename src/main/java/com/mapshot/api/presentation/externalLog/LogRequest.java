package com.mapshot.api.presentation.externalLog;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LogRequest {
    private String roll;
    private String stackTrace;
}
