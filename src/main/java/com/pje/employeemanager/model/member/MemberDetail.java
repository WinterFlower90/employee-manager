package com.pje.employeemanager.model.member;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.enums.Gender;
import com.pje.employeemanager.enums.Position;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {

    private String employeeNumber; //사원번호

    private String name; //이름

    private String phone; //연락처

    private LocalDate birthday; //생년월일

    private String gender; //성별

    public String username;

    public String password;

    private String department; //부서

    private String position; //직급

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
            this.username = member.getUserId();
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
