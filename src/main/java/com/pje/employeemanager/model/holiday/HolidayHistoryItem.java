package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayHistoryItem {
    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "[부서] 사원 이름 + 사원 직급")
    private String memberFullName;

    @ApiModelProperty(notes = "희망 일자")
    private LocalDate dateDesired;

    @ApiModelProperty(notes = "연차 종류")
    private String holidayType;

    @ApiModelProperty(notes = "연차 사유")
    private String reason;

    private HolidayHistoryItem(HolidayHistoryItemBuilder builder) {
        this.memberId = builder.memberId;
        this.memberFullName = builder.memberFullName;
        this.dateDesired = builder.dateDesired;
        this.holidayType = builder.holidayType;
        this.reason = builder.reason;
    }

    public static class HolidayHistoryItemBuilder implements CommonModelBuilder<HolidayHistoryItem> {
        private Long memberId;
        private String memberFullName;
        private LocalDate dateDesired;
        private String holidayType;
        private String reason;

        public HolidayHistoryItemBuilder(HolidayHistory holidayHistory) {
            this.memberId = holidayHistory.getMember().getId();
            this.memberFullName = "[" + holidayHistory.getMember().getDepartment().getName() + "] " + holidayHistory.getMember().getName() + " " + holidayHistory.getMember().getPosition().getName();
            this.dateDesired = holidayHistory.getDateDesired();
            this.holidayType = holidayHistory.getHolidayType().getName();
            this.reason = holidayHistory.getReason();
        }

        @Override
        public HolidayHistoryItem build() {
            return new HolidayHistoryItem(this);
        }
    }
}
