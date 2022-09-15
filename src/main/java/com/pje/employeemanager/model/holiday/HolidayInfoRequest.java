package com.pje.employeemanager.model.holiday;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class HolidayInfoRequest {
    @ApiModelProperty(notes = "사원 시퀀스", required = true)
    @NotNull
    private Long memberId;

    @ApiModelProperty(notes = "총 연차 갯수", required = true)
    @NotNull
    private Integer holidayTotal; //총 연차 갯수

    @ApiModelProperty(notes = "사용 연차", required = true)
    @NotNull
    private Float holidayUse; //사용 연차

    @ApiModelProperty(notes = "남은 연차 갯수", required = true)
    @NotNull
    private Float holidayRemain; //남은 연차 갯수
}
