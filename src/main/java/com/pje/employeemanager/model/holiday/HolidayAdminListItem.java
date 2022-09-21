package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.entity.HolidayCount;
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
public class HolidayAdminListItem {
    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "휴가 기록 시퀀스")
    private Long holidayHistoryId;

    @ApiModelProperty(notes = "사원 아이디")
    private String username;

    @ApiModelProperty(notes = "사원 이름")
    private String name;

    @ApiModelProperty(notes = "휴가 타입 - 연차.반차 등")
    private String holidayType; //휴가 타입

    @ApiModelProperty(notes = "휴가 승인 상태")
    private String holidayStatus; //휴가 승인 상태

    @ApiModelProperty(notes = "휴가 사유")
    private String reason; //휴가 사유

    @ApiModelProperty(notes = "휴가 희망 일자")
    private LocalDate dateDesired; //희망 일자

    @ApiModelProperty(notes = "휴가 신청 일자")
    private LocalDate dateApplication; //신청 일자

    @ApiModelProperty(notes = "휴가 승인 시간")
    private LocalDateTime dateApproval; //승인 시간 (update 개념)

    @ApiModelProperty(notes = "휴가 반려 시간")
    private LocalDateTime dateRefusal; //반려 시간 (update 개념)


    private HolidayAdminListItem(HolidayAdminListItemBuilder builder) {
        this.memberId = builder.memberId;
        this.holidayHistoryId = builder.holidayHistoryId;
        this.username = builder.username;
        this.name = builder.name;
        this.holidayType = builder.holidayType;
        this.holidayStatus = builder.holidayStatus;
        this.reason = builder.reason;
        this.dateDesired = builder.dateDesired;
        this.dateApplication = builder.dateApplication;
        this.dateApproval = builder.dateApproval;
        this.dateRefusal = builder.dateRefusal;
    }

    public static class HolidayAdminListItemBuilder implements CommonModelBuilder<HolidayAdminListItem> {
        private final Long memberId;
        private final Long holidayHistoryId;
        private final String username;
        private final String name;
        private final String holidayType; //휴가 타입
        private final String holidayStatus; //휴가 승인 상태
        private final String reason; //휴가 사유
        private final LocalDate dateDesired; //희망 일자
        private final LocalDate dateApplication; //신청 일자
        private final LocalDateTime dateApproval; //승인 시간 (update 개념)
        private final LocalDateTime dateRefusal; //반려 시간 (update 개념)

        public HolidayAdminListItemBuilder(HolidayHistory holidayHistory) {
            this.memberId = holidayHistory.getMember().getId();
            this.holidayHistoryId = holidayHistory.getId();
            this.username = holidayHistory.getMember().getUsername();
            this.name = holidayHistory.getMember().getName();
            this.holidayType = holidayHistory.getHolidayType().getName();
            this.holidayStatus = holidayHistory.getHolidayStatus().getName();
            this.reason = holidayHistory.getReason();
            this.dateDesired = holidayHistory.getDateDesired();
            this.dateApplication = holidayHistory.getDateApplication();
            this.dateApproval = holidayHistory.getDateApproval();
            this.dateRefusal = holidayHistory.getDateRefusal();
        }

        @Override
        public HolidayAdminListItem build() {
            return new HolidayAdminListItem(this);
        }
    }
}
