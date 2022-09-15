package com.pje.employeemanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkStatus {
    ATTENDANCE ("출근"), //출근 상태일때만 외출, 조퇴, 퇴근이 보여야한다.
    GOING_OUT ("외출"), //외출 상태일때만 복귀버튼이 보여진다.
    RETURN ("복귀"), //외출 상태일때만 복귀버튼이 보여진다.
    LEAVE_WORK ("퇴근"),
    NO_STATUS ("상태없음") //상태없음 일때만 출근버튼이 보여야한다.
    ;
    private final String name;
}
