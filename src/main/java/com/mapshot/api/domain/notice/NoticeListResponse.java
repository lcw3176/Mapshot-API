package com.mapshot.api.domain.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeListResponse {

    private List<NoticeDto> notices = new ArrayList<>();
    private int totalPage;
}