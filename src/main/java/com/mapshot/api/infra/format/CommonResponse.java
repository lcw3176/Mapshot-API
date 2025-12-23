package com.mapshot.api.infra.format;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> {


//    // 응답 상태 코드
//    http 요청 자체의 성공여부랑 해당 요청의 성공여부랑 구분하려면 필요할것 같은데
//    뭐 그정도까지 해야되는 프로젝트는 아닌듯 해서....
//    private int code;

    // 응답 메시지
    private String message;

    // 실제 데이터
    private T data;

    // 응답 시간
    private LocalDateTime timestamp;

    // 성공 응답 생성 메서드
    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .message("success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 생성 메서드 (커스텀 메시지)
    public static <T> CommonResponse<T> success(String message, T data) {
        return CommonResponse.<T>builder()
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답 생성 메서드
    public static <T> CommonResponse<T> fail(String message) {
        return CommonResponse.<T>builder()
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
