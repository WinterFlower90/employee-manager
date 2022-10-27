package com.pje.employeemanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    STAFF ("사원"),
    DEPUTY ("대리"),
    SECTION_CHIEF ("과장"),
    SECTION_HEAD ("부장");

    private final String name;
}
