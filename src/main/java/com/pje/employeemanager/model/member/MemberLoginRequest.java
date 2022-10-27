package com.pje.employeemanager.model.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberLoginRequest {

    @ApiModelProperty(value = "사원 아이디", required = true)
    @NotNull
    @Length(min = 6, max = 20)
    private String username; //사원 아이디

    @ApiModelProperty(value = "사원 아이디", required = true)
    @NotNull
    @Length(min = 8, max = 20)
    private String password; //사원 비밀번호
}
