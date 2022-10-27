package com.pje.employeemanager.model.holiday;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class HolidayHistorySearchRequest {
    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "연차 기준 년도")
    @NotNull
    private Integer holidayDateYear;

    @ApiModelProperty(notes = "연차 기준 월")
    @NotNull
    private Integer holidayDateMonth;

}
