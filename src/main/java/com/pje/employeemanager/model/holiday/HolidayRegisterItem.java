package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayRegisterItem {
    @ApiModelProperty(notes = "휴가 신청 시퀀스")
    private Long holidayHistoryId; //휴가신청 시퀀스

    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId; //사원 시퀀스

    @ApiModelProperty(notes = "[부서] 사원 이름 + 사원 직급")
    private String memberFullName; // [부서] 사원 이름 + 사원 직급

    @ApiModelProperty(notes = "휴가 타입")
    private String holidayType; //휴가 종류 - 연차, 병가 ...

    @ApiModelProperty(notes = "휴가 사유")
    private String reason; //휴가 사유

    @ApiModelProperty(notes = "휴가 희망 일자")
    private LocalDate dateDesired; //휴가 희망 일자

    @ApiModelProperty(notes = "휴가 신청 일자")
    private LocalDate dateApplication; //휴가 신청 일자

    @ApiModelProperty(notes = "휴가 승인 상태")
    private String holidayStatus; //승인 상태

    @ApiModelProperty(notes = "휴가 승인 시간")
    private LocalDateTime dateApproval; //승인 시간

    private HolidayRegisterItem(HolidayRegisterItemBuilder builder) {
        this.holidayHistoryId = builder.holidayHistoryId;
        this.memberId = builder.memberId;
        this.memberFullName = builder.memberFullName;
        this.holidayType = builder.holidayType;
        this.reason = builder.reason;
        this.dateDesired = builder.dateDesired;
        this.dateApplication = builder.dateApplication;
        this.holidayStatus = builder.holidayStatus;
        this.dateApproval = builder.dateApproval;
    }

    public static class HolidayRegisterItemBuilder implements CommonModelBuilder<HolidayRegisterItem> {
        private final Long holidayHistoryId; //휴가신청 시퀀스
        private final Long memberId; //사원 시퀀스
        private final String memberFullName; // [부서] 사원 이름 + 사원 직급
        private final String holidayType; //휴가 종류 - 연차, 병가 ...
        private final String reason; //휴가 사유
        private final LocalDate dateDesired; //휴가 희망 일자
        private final LocalDate dateApplication; //휴가 신청 일자
        private final String holidayStatus; //승인 상태
        private final LocalDateTime dateApproval; //승인 시간

        public HolidayRegisterItemBuilder(HolidayHistory holidayHistory) {
            this.holidayHistoryId = holidayHistory.getId();
            this.memberId = holidayHistory.getMember().getId();
            this.memberFullName = "[" + holidayHistory.getMember().getDepartment().getName() + "] " + holidayHistory.getMember().getName() + " " + holidayHistory.getMember().getPosition().getName();
            this.holidayType = holidayHistory.getHolidayType().getName();
            this.reason = holidayHistory.getReason();
            this.dateDesired = holidayHistory.getDateDesired();
            this.dateApplication = holidayHistory.getDateApplication();
            this.holidayStatus = holidayHistory.getHolidayStatus().getName();
            this.dateApproval = holidayHistory.getDateApproval();
        }

        @Override
        public HolidayRegisterItem build() {
            return new HolidayRegisterItem(this);
        }
    }
}
