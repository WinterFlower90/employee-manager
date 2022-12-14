package com.pje.employeemanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(0,"성공하였습니다.")
    ,FAILED(-1, "실패하였습니다.")

    ,MISSING_DATA(-10000,"데이터를 찾을 수 없습니다.")

    ,NO_MEMBER_DATA(-20000, "회원정보가 없습니다.")
    ,NO_MEMBER_WORKING(-20001, "퇴사한 사원입니다. 관리자에게 문의하세요.")

    ,NOT_EQUAL_PASSWORD(-30000, "잘못된 비밀번호입니다.")
    ,WRONG_PHONE_NUMBER(-30001, "잘못된 핸드폰 번호입니다.")

    ,NO_WORK_DATA(-40000, "출근 기록이 없습니다.")
    ,NOT_SAME_STATUS(-40001, "같은 근태상태로는 다시 변경 할 수 없습니다.")
    ,ALREADY_WORK_IN(-40002, "근태상태를 다시 출근으로 변경 할 수 없습니다.")
    ,ALREADY_WORK_OUT(-40003, "퇴근 후에는 상태를 다시 변경할 수 없습니다.")

    ,NO_HOLIDAY_COUNT_REMAIN(-50000, "남은 연차 수량이 부족합니다.")
    ,ALREADY_HOLIDAY_STATUS(-50001, "이미 휴가 처리가 완료되었습니다.")
    ,OVERLAP_HOLIDAY_DATA(-50002, "이미 동일한 날짜에 신청한 휴가가 있습니다.")
    ;


    private final Integer code;
    private final String msg;
}

