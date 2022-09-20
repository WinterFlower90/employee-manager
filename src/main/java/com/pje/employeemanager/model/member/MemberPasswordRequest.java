package com.pje.employeemanager.model.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberPasswordRequest {
    @ApiModelProperty(notes = "사원 비밀번호(~20글자)", required = true)
    @Length(max = 20)
    @NotNull
    private String password; //사용자 비밀번호
}
