package com.pje.employeemanager.model.member;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MemberAdminListItem {

    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId; //사원 시퀀스

    @ApiModelProperty(notes = "사원 아이디")
    private String username; //사원 아이디

    @ApiModelProperty(notes = "사원 연락처")
    private String phone; //사원 연락처

    @ApiModelProperty(notes = "사원 성별")
    private String gender; //성별

    @ApiModelProperty(notes = "사원 부서")
    private String department; //부서

    @ApiModelProperty(notes = "사원 직급")
    private String position; //직급

    @ApiModelProperty(notes = "재직 여부")
    private Boolean isWorking; //재직 여부

    @ApiModelProperty(notes = "사원 입사일")
    private LocalDate dateJoin; //입사일

    @ApiModelProperty(notes = "사원 퇴사일")
    private LocalDate dateRetire; //퇴사일

    @ApiModelProperty(notes = "관리자 여부")
    private Boolean isManager; // 관리자 여부

    private MemberAdminListItem(MemberAdminListItemBuilder builder) {
        this.memberId = builder.memberId;
        this.username = builder.username;
        this.phone = builder.phone;
        this.gender = builder.gender;
        this.department = builder.department;
        this.position = builder.position;
        this.isWorking = builder.isWorking;
        this.dateJoin = builder.dateJoin;
        this.dateRetire = builder.dateRetire;
        this.isManager = builder.isManager;
    }

    public static class MemberAdminListItemBuilder implements CommonModelBuilder<MemberAdminListItem> {
        private final Long memberId; //사원 시퀀스
        private final String username; //사원 아이디
        private final String phone; //사원 연락처
        private final String gender; //성별
        private final String department; //부서
        private final String position; //직급
        private final Boolean isWorking; //재직 여부
        private final LocalDate dateJoin; //입사일
        private final LocalDate dateRetire; //퇴사일
        private final Boolean isManager; // 관리자 여부

        public MemberAdminListItemBuilder(Member member) {
            this.memberId = member.getId();
            this.username = member.getUsername();
            this.phone = member.getPhone();
            this.gender = member.getGender().getName();
            this.department = member.getDepartment().getName();
            this.position = member.getPosition().getName();
            this.isWorking = member.getIsWorking();
            this.dateJoin = member.getDateJoin();
            this.dateRetire = member.getDateRetire();
            this.isManager = member.getIsManager();
        }

        @Override
        public MemberAdminListItem build() {
            return new MemberAdminListItem(this);
        }
    }
}
