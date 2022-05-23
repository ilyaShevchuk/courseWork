package com.tinkoff.timetable.security.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    READ("read"),
    CREATE("create"),
    TEACHER_CONTROL("teacher:all"),
    REFACTOR_ALL("update any entity"),
    DELETE("delete");

    private final String permission;
}
