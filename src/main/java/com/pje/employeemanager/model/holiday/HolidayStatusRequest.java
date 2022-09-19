package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.enums.HolidayStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class HolidayStatusRequest {
    @ApiModelProperty(notes = "휴가 승인 상태", required = true)
    @Enumerated(value = EnumType.STRING)
    private HolidayStatus holidayStatus;
}
