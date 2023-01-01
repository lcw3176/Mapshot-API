package com.joebrooks.mapshotapi.notice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PageGenerator {


    /**
     * @param nowPage 현재 유저가 요청한 페이지 번호
     * @return 유저의 현재 페이지 범위에 맞는 첫 페이지. 범위는 10으로 설정되어 있음.
     * <p>
     * ex) nowPage: 1, return: 1 nowPage: 11, return: 11 nowPage: 15, return: 11 nowPage: 24, return: 21
     */
    public int getStartPage(int nowPage) {
        return Math.max((nowPage - 1) / 10 * 10 + 1, 1);
    }


    /**
     * @param nowPage          현재 유저가 요청한 페이지 번호
     * @param totalElementSize 전체 컨텐츠 갯수
     * @return 유저의 현제 페이지 범위에 맞는 마지막 페이지. 범위는 10으로 설정되어 있음.
     * <p>
     * ex) nowPage: 1, return: 10 nowPage: 11, return: 20 nowPage: 15, return: 20 nowPage: 24, return: 30
     */
    public int getEndPage(int nowPage, long totalElementSize) {
        if (nowPage % 10 == 0) {
            nowPage -= 1;
        }

        return (int) Math.min((nowPage / 10 + 1) * 10, ((totalElementSize - 1) / 10) + 1);
    }


}