package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.entity.TestHolidayCount;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyHolidayCountResponse {
    @ApiModelProperty(notes = "연차 기준 시작일")
    private LocalDate dateCriteriaStart;

    @ApiModelProperty(notes = "연차 기준 종료일")
    private LocalDate dateCriteriaEnd;

    @ApiModelProperty(notes = "총 연차 개수")
    private Integer countTotal;

    @ApiModelProperty(notes = "사용한 연차 개수")
    private Long countUse;

    @ApiModelProperty(notes = "잔여(미사용) 연차 개수")
    private Long countRemain;

    private MyHolidayCountResponse(MyHolidayCountResponseBuilder builder) {
        this.dateCriteriaStart = builder.dateCriteriaStart;
        this.dateCriteriaEnd = builder.dateCriteriaEnd;
        this.countTotal = builder.countTotal;
        this.countUse = builder.countUse;
        this.countRemain = builder.countRemain;
    }

    private MyHolidayCountResponse(MyHolidayCountResponseEmptyBuilder builder) {
        this.dateCriteriaStart = builder.dateCriteriaStart;
        this.dateCriteriaEnd = builder.dateCriteriaEnd;
        this.countTotal = builder.countTotal;
        this.countUse = builder.countUse;
        this.countRemain = builder.countRemain;
    }

    public static class MyHolidayCountResponseBuilder implements CommonModelBuilder<MyHolidayCountResponse> {
        private final LocalDate dateCriteriaStart;
        private final LocalDate dateCriteriaEnd;
        private final Integer countTotal;
        private final Long countUse;
        private final Long countRemain;

        public MyHolidayCountResponseBuilder(TestHolidayCount holidayCount, Long countComplete) {
            this.dateCriteriaStart = holidayCount.getDateStart();
            this.dateCriteriaEnd = holidayCount.getDateEnd();
            this.countTotal = holidayCount.getCountTotal();
            this.countUse = countComplete;
            this.countRemain = this.countTotal - this.countUse;
        }
        @Override
        public MyHolidayCountResponse build() {
            return new MyHolidayCountResponse(this);
        }
    }

    public static class MyHolidayCountResponseEmptyBuilder implements CommonModelBuilder<MyHolidayCountResponse> {
        private final LocalDate dateCriteriaStart;
        private final LocalDate dateCriteriaEnd;
        private final Integer countTotal;
        private final Long countUse;
        private final Long countRemain;

        public MyHolidayCountResponseEmptyBuilder(LocalDate dateCriteriaStart, LocalDate dateCriteriaEnd, Long countComplete) {
            this.dateCriteriaStart = dateCriteriaStart;
            this.dateCriteriaEnd = dateCriteriaEnd;
            this.countTotal = 0;
            this.countUse = countComplete;
            this.countRemain = this.countTotal - this.countUse;
        }

        @Override
        public MyHolidayCountResponse build() {
            return new MyHolidayCountResponse(this);
        }
    }
}

