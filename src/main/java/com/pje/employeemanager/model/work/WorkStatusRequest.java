package com.pje.employeemanager.model.work;

import com.pje.employeemanager.enums.WorkStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class WorkStatusRequest {
    @ApiModelProperty(notes = "근무 상태 타입", required = true)
    @Enumerated(value = EnumType.STRING)
    private WorkStatus workStatus; //상태

}
