package com.pje.employeemanager.model.work;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkStatusCountResponse {
    @ApiModelProperty(notes = "출근 횟수")
    private Long countAttendance;

    @ApiModelProperty(notes = "조퇴 횟수")
    private Long countEarlyLeave;

    @ApiModelProperty(notes = "결근 횟수")
    private Long countNoStatus;
}
