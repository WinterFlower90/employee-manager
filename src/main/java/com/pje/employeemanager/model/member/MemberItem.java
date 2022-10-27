package com.pje.employeemanager.model.member;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberItem {
    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "사원 번호")
    private String employeeNumber;

    @ApiModelProperty(notes = "관리자 여부")
    private String isManager;

    @ApiModelProperty(notes = "[부서] 사원 이름 + 사원 직급")
    private String memberFullName;

    @ApiModelProperty(notes = "사원 연락처")
    private String memberPhone;

    @ApiModelProperty(notes = "사원 생년월일")
    private LocalDate birthday;

    @ApiModelProperty(notes = "사원 성별")
    private String gender;

    @ApiModelProperty(notes = "사원 프로필 사진 url")
    private String profileImageUrl; //프로필 사진 url

    @ApiModelProperty(notes = "사원 재직 여부")
    private String isWorking;

    @ApiModelProperty(notes = "사원 입사 일자")
    private LocalDate dateJoin;

    @ApiModelProperty(notes = "사원 퇴사 일자")
    private LocalDate dateRetire;


    private MemberItem(MemberItemBuilder builder) {
        this.memberId = builder.memberId;
        this.employeeNumber = builder.employeeNumber;
        this.isManager = builder.isManager;
        this.memberFullName = builder.memberFullName;
        this.memberPhone = builder.memberPhone;
        this.birthday = builder.birthday;
        this.gender = builder.gender;
        this.profileImageUrl = builder.profileImageUrl;
        this.isWorking = builder.isWorking;
        this.dateJoin = builder.dateJoin;
        this.dateRetire = builder.dateRetire;
    }

    public static class MemberItemBuilder implements CommonModelBuilder<MemberItem> {
        private final Long memberId;
        private final String employeeNumber;
        private final String isManager;
        private final String memberFullName;
        private final String memberPhone;
        private final LocalDate birthday;
        private final String gender;
        private final String profileImageUrl; //프로필 사진 url
        private final String isWorking;
        private final LocalDate dateJoin;
        private final LocalDate dateRetire;

        public MemberItemBuilder(Member member) {
            this.memberId = member.getId();
            this.employeeNumber = member.getEmployeeNumber();
            this.isManager = member.getIsManager() ? "예" : "아니오" ;
            this.memberFullName = "[" + member.getDepartment().getName() + "] " + member.getName() + " " + member.getPosition().getName();
            this.memberPhone = member.getPhone();
            this.birthday = member.getBirthday();
            this.gender = member.getGender().getName();
            this.profileImageUrl = member.getProfileImageUrl();
            this.isWorking = member.getIsWorking() ? "예" : "아니오" ;
            this.dateJoin = member.getDateJoin();
            this.dateRetire = member.getDateRetire();
        }


        @Override
        public MemberItem build() {
            return new MemberItem(this);
        }
    }
}
