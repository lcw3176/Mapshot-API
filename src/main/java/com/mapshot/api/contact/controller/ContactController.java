package com.mapshot.api.contact.controller;

import com.mapshot.api.auth.annotation.PreAuth;
import com.mapshot.api.auth.enums.Accessible;
import com.mapshot.api.contact.model.ContactDetailResponse;
import com.mapshot.api.contact.model.ContactListResponse;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class ContactController {


    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/list/{pageNumber}")
    public ResponseEntity<List<ContactListResponse>> showContactList(
            @PositiveOrZero @PathVariable(value = "pageNumber") long pageNumber) {

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{id}")
    public ResponseEntity<ContactDetailResponse> showSpecificContact(
            @Positive @PathVariable(value = "id") long id) {

        return ResponseEntity.ok().build();
    }
}
