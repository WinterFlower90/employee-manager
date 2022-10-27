package com.pje.employeemanager.model.member;

import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.enums.Gender;
import com.pje.employeemanager.enums.Position;
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
public class MemberJoinRequest {
    @ApiModelProperty(notes = "사원 번호(~10글자)", required = true)
    @Length(max = 10)
    @NotNull
    private String employeeNumber; //사원번호

    @ApiModelProperty(notes = "사원 이름(~20글자)", required = true)
    @Length(max = 20)
    @NotNull
    private String name; //이름

    @ApiModelProperty(notes = "사원 연락처(~15글자)", required = true)
    @Length(max = 15)
    @NotNull
    private String phone; //연락처


    @ApiModelProperty(notes = "사원 생년월일", required = true)
    @NotNull
    private LocalDate birthday; //생년월일

    @ApiModelProperty(notes = "사원 성별(~10글자)", required = true)
    @Enumerated(value = EnumType.STRING)
    private Gender gender; //성별

    @ApiModelProperty(notes = "사원 부서(~20글자)", required = true)
    @Enumerated(value = EnumType.STRING)
    private Department department; //부서

    @ApiModelProperty(notes = "사원 직급(~20글자)", required = true)
    @Enumerated(value = EnumType.STRING)
    private Position position; //직급

    @ApiModelProperty(notes = "프로필사진 url", required = false)
    private String profileImageUrl; //프로필 사진 url

    @ApiModelProperty(notes = "사원 아이디(6글자 ~ 20글자)", required = true)
    @Length(min = 6, max = 20)
    @NotNull
    private String username; //사용자 아이디

    @ApiModelProperty(notes = "사원 비밀번호(8글자 ~ 20글자)", required = true)
    @Length(min = 8, max = 20)
    @NotNull
    private String password; //사용자 비밀번호
}
