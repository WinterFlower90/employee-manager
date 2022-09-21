package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;

public class HolidayCountListItem {
    @ApiModelProperty(notes = "회원 시퀀스")
    private Long memberId;
    @ApiModelProperty(notes = "총 연차 갯수")
    private Float countTotal; //총 연차 갯수

    @ApiModelProperty(notes = "사용 연차 갯수")
    private Float countUse; //사용 연차 갯수

    @ApiModelProperty(notes = "남은 연차 갯수")
    private Float countRemain; //남은 연차 갯수

    private HolidayCountListItem(HolidayCountListItemBuilder builder) {
        this.memberId = builder.memberId;
        this.countTotal = builder.countTotal;
        this.countUse = builder.countUse;
        this.countRemain = builder.countRemain;
    }

    public static class HolidayCountListItemBuilder implements CommonModelBuilder<HolidayCountListItem> {
        private final Long memberId;
        private final Float countTotal; //총 연차 갯수
        private final Float countUse; //사용 연차 갯수
        private final Float countRemain; //남은 연차 갯수

        public HolidayCountListItemBuilder(HolidayCount holidayCount) {
            this.memberId = holidayCount.getMemberId();
            this.countTotal = holidayCount.getCountTotal();
            this.countUse = holidayCount.getCountUse();
            this.countRemain = holidayCount.getHolidayRemain();
        }

        @Override
        public HolidayCountListItem build() {
            return new HolidayCountListItem(this);
        }
    }
}
