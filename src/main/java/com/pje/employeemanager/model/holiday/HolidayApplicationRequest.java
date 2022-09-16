package com.pje.employeemanager.model.holiday;

import com.pje.employeemanager.enums.HolidayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class HolidayApplicationRequest {
    @ApiModelProperty(notes = "사원 시퀀스", required = true)
    @NotNull
    private Long memberId;

    @ApiModelProperty(notes = "휴가 타입", required = true)
    @Enumerated(value = EnumType.STRING)
    private HolidayType holidayType;

    @ApiModelProperty(notes = "휴가 사유", required = true)
    @NotNull
    private String reason;

    @ApiModelProperty(notes = "희망 날짜", required = true)
    @NotNull
    private LocalDate dateDesired;

}
