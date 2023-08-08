package com.mapshot.api.contact.entity;

import com.mapshot.api.common.entity.BaseTimeEntity;
import com.mapshot.api.contact.consts.ContactConfig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "contact")
public class ContactEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = ContactConfig.MAX_WRITER_LENGTH)
    private String writer;

    @Column(length = ContactConfig.MAX_CONTENT_LENGTH)
    private String content;

    private String ipAddress;


}
