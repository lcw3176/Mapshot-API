package com.mapshot.api.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType {
    UPDATE("업데이트"),
    FIX("오류수정"),
    RESERVED_CHECK("점검예정");

    private final String korean;
    
}