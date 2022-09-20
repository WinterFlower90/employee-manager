package com.pje.employeemanager.model.holiday;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayCountRequest {
    @ApiModelProperty(notes = "연차 증감 상태. true - 차감, false - 증가", required = true)
    private boolean isMinus;

    @ApiModelProperty(notes = "휴가 증감 값", required = true)
    private float increaseOrDecreaseValue;
}
