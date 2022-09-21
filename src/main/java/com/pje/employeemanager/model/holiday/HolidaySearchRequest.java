package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.enums.HolidayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
public class HolidaySearchRequest {
    /** 검색 필터 설계하기.
     아무 필터도 설정하지 않으면 전체 리스트가 나오는것이 포인트 */

    @ApiModelProperty(notes = "사원 아이디")
    private String username; // like 검색

    @ApiModelProperty(notes = "사원 이름")
    private String name;  // like 검색

    @ApiModelProperty(notes = "휴가 타입 : 연차, 반차 등")
    @Enumerated(value = EnumType.STRING)
    private HolidayType holidayType; //휴가 타입 : 연차, 반차, 병가 ... 일치하는것 검색

    @ApiModelProperty(notes = "휴가 승인 여부")
    @Enumerated(value = EnumType.STRING)
    private HolidayStatus holidayStatus; //승인 여부 - 검토중 / 승인 / 반려 - 일치하는것 검색

    @ApiModelProperty(notes = "휴가 희망 일자 (검색 시작일)")
    private LocalDate dateDesiredStart; //희망 일자

    @ApiModelProperty(notes = "휴가 희망 일자 (검색 종료일)")
    private LocalDate dateDesiredEnd; //희망 일자

    @ApiModelProperty(notes = "휴가 신청 일자 (검색 시작일)")
    private LocalDate dateApplicationStart; //신청 일자

    @ApiModelProperty(notes = "휴가 신청 일자 (검색 종료일)")
    private LocalDate dateApplicationEnd; //신청 일자
}
