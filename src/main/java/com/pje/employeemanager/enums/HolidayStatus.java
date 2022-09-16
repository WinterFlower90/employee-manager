package com.pje.employeemanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayStatus {
    OK ("승인"),
    CANCEL ("반려"),
    NO_STATUS ("검토중")
    ;

    private final String name;
}
