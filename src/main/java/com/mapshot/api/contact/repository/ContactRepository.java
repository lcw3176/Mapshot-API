package com.mapshot.api.contact.repository;

import com.mapshot.api.contact.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
