package com.mapshot.api.domain.news.client.gov;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportGovResponse {
    private LocalDate uploadDate;
    private String keyword;
}
