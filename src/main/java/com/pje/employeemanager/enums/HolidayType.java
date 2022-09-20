package com.pje.employeemanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayType {
    ANNUAL ("연차"),
    HALF_ANNUAL ("반차"),
    SICK ("병가")
    ;

    private final String name;
}
