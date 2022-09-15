package com.pje.employeemanager.model.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberPersonalInformationRequest {
    @ApiModelProperty(notes = "사원 이름(~20글자)", required = true)
    @Length(max = 20)
    @NotNull
    private String name; //이름

    @ApiModelProperty(notes = "사원 연락처(~15글자)", required = true)
    @Length(max = 15)
    @NotNull
    private String phone; //연락처

    @ApiModelProperty(notes = "프로필사진 url", required = false)
    @Enumerated(value = EnumType.STRING)
    private String profileImageUrl; //프로필 사진 url


}
