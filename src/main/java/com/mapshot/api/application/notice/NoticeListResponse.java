package com.mapshot.api.application.notice;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeListResponse {

    private List<NoticeDto> notices;
    private int totalPage;
}
