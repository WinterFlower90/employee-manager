package com.pje.employeemanager.model.member;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.enums.Gender;
import com.pje.employeemanager.enums.Position;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {

    @ApiModelProperty(notes = "사원 번호")
    private String employeeNumber; //사원번호

    @ApiModelProperty(notes = "사원 이름")
    private String name; //이름

    @ApiModelProperty(notes = "사원 연락처")
    private String phone; //연락처

    @ApiModelProperty(notes = "사원 생년월일")
    private LocalDate birthday; //생년월일

    @ApiModelProperty(notes = "사원 성별")
    private String gender; //성별

    @ApiModelProperty(notes = "사원 아이디")
    public String username;

    @ApiModelProperty(notes = "사원 비밀번호")
    public String password;

    @ApiModelProperty(notes = "사원 부서")
    private String department; //부서

    @ApiModelProperty(notes = "사원 직급")
    private String position; //직급

    @ApiModelProperty(notes = "사원 입사일")
    private LocalDate dateJoin; //입사일

    private MemberDetail(MemberDetailBuilder builder) {
        this.employeeNumber = builder.employeeNumber;
        this.name = builder.name;
        this.phone = builder.phone;
        this.birthday = builder.birthday;
        this.gender = builder.gender;
        this.username = builder.username;
        this.password = builder.password;
        this.department = builder.department;
        this.position = builder.position;
        this.dateJoin = builder.dateJoin;

    }

    public static class MemberDetailBuilder implements CommonModelBuilder<MemberDetail> {
        private final String employeeNumber; //사원번호
        private final String name; //이름
        private final String phone; //연락처
        private final LocalDate birthday; //생년월일
        private final String gender; //성별
        public final String username;
        public final String password;
        private final String department; //부서
        private final String position; //직급
        private final LocalDate dateJoin; //입사일

        public MemberDetailBuilder(Member member) {
            this.employeeNumber = member.getEmployeeNumber();
            this.name = member.getName();
            this.phone = member.getPhone();
            this.birthday = member.getBirthday();
            this.gender = member.getGender().getName();
            this.username = member.getUsername();
            this.password = member.getPassword();
            this.department = member.getDepartment().getName();
            this.position = member.getPosition().getName();
            this.dateJoin = member.getDateJoin();
        }

        @Override
        public MemberDetail build() {
            return new MemberDetail(this);
        }
    }
}
