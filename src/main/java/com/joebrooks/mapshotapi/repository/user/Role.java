package com.joebrooks.mapshotapi.repository.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN,
    USER;
}