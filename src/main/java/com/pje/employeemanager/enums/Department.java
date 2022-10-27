package com.pje.employeemanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Department {
    PERSONNEL ("인사부"),
    PLANNING ("기획부"),
    SALES ("영업부"),
    ACCOUNTING ("경리부");

    private final String name;
}
