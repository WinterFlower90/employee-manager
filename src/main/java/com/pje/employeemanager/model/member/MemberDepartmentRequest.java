package com.pje.employeemanager.model.member;

import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.enums.Position;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class MemberDepartmentRequest {
    @ApiModelProperty(notes = "사원 부서(~20글자)", required = true)
    @Enumerated(value = EnumType.STRING)
    private Department department; //부서

    @ApiModelProperty(notes = "사원 직급(~20글자)", required = true)
    @Enumerated(value = EnumType.STRING)
    private Position position; //직급

}
