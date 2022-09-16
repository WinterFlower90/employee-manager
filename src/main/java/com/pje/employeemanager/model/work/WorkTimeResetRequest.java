package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
public class WorkTimeResetRequest {

    private Member member;

    @ApiModelProperty(notes = "출근 시간", required = true)
    @NotNull
    private LocalTime inWork;

    @ApiModelProperty(notes = "퇴근 시간", required = true)
    @NotNull
    private LocalTime outWork;
}
