package com.pje.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(0,"성공하였습니다.")
    ,FAILED(-1, "실패하였습니다.")

    ,MISSING_DATA(-10000,"데이터를 찾을 수 없습니다.")
    ,WRONG_PHONE_NUMBER(-10001, "잘못된 핸드폰 번호입니다.")

    ,NO_MEMBER_DATA(-20000, "회원정보가 없습니다.")
    ;


    private final Integer code;
    private final String msg;
}

