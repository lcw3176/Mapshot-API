//package com.joebrooks.mapshotapi;
//
//
//import com.joebrooks.mapshotapi.repository.notice.NoticeEntity;
//import com.joebrooks.mapshotapi.repository.notice.NoticeService;
//import com.joebrooks.mapshotapi.repository.notice.NoticeType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class DBDataInjectionScript {
//
//    @Autowired
//    private NoticeService noticeService;
//
//    @Test
//    void 공지사항_더미_데이터_주입() {
//        for (int i = 0; i <= 1000; i++) {
//            String temp = Integer.toString(i);
//
//            noticeService.addPost(NoticeEntity.builder()
//                    .noticeType(NoticeType.UPDATE)
//                    .title(temp)
//                    .content(temp)
//                    .build());
//        }
//
//    }
//
//
//}
