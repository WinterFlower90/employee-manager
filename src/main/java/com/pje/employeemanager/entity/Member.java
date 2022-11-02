package com.pje.employeemanager.entity;

import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.enums.Gender;
import com.pje.employeemanager.enums.Position;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import com.pje.employeemanager.model.member.MemberDepartmentRequest;
import com.pje.employeemanager.model.member.MemberJoinRequest;
import com.pje.employeemanager.model.member.MemberPasswordRequest;
import com.pje.employeemanager.model.member.MemberPersonalInformationRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "관리자 여부")
    @Column(nullable = false)
    private Boolean isManager; //관리자 여부

    @ApiModelProperty(notes = "사원 번호")
    @Column(nullable = false, length = 10)
    private String employeeNumber; //사원번호

    @ApiModelProperty(notes = "사원 이름")
    @Column(nullable = false, length = 20)
    private String name; //이름

    @ApiModelProperty(notes = "사원 연락처")
    @Column(nullable = false, length = 15)
    private String phone; //연락처

    @ApiModelProperty(notes = "사원 생년월일")
    @Column(nullable = false)
    private LocalDate birthday; //생년월일

    @ApiModelProperty(notes = "사원 성별")
    @Column(nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private Gender gender; //성별

    @ApiModelProperty(notes = "부서")
    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private Department department; //부서

    @ApiModelProperty(notes = "직급")
    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private Position position; //직급

    @ApiModelProperty(notes = "프로필사진 url")
    @Column(nullable = true)
    private String profileImageUrl; //프로필 사진 url

    @ApiModelProperty(notes = "입사일")
    @Column(nullable = false)
    private LocalDate dateJoin; //입사일

    @ApiModelProperty(notes = "재직 여부")
    @Column(nullable = false)
    private Boolean isWorking; //재직 여부

    @ApiModelProperty(notes = "퇴사일")
    @Column(nullable = true)
    private LocalDate dateRetire; //퇴사일

    @ApiModelProperty(notes = "사용자 아이디")
    @Column(nullable = false, length = 20, unique = true)
    private String username; //사용자 아이디

    @ApiModelProperty(notes = "사용자 비밀번호")
    @Column(nullable = false, length = 20)
    private String password; //사용자 비밀번호

    @ApiModelProperty(notes = "등록 시간")
    @Column(nullable = false)
    private LocalDateTime dateCreate; //등록시간

    @ApiModelProperty(notes = "수정 시간")
    @Column(nullable = false)
    private LocalDateTime dateUpdate; //수정시간

    /** 사원 비밀번호 변경하기 - 사원. 관리자 가능*/
    public void putPassword(MemberPasswordRequest passwordRequest) {
        this.password = passwordRequest.getPassword();
        this.dateUpdate = LocalDateTime.now();
    }

    /** 사원 부서 및 직급 변경하기 - 관리자 가능 */
    public void putDepartment(MemberDepartmentRequest departmentRequest) {
        this.department = departmentRequest.getDepartment();
        this.position = departmentRequest.getPosition();
        this.dateUpdate = LocalDateTime.now();
    }

    /** 사원 이름/연락처/프로필사진 변경하기 - 사원. 관리자 가능*/
    public void putPersonalInfo(MemberPersonalInformationRequest personalInformationRequest) {
        this.name = personalInformationRequest.getName();
        this.phone = personalInformationRequest.getPhone();
        this.profileImageUrl = personalInformationRequest.getProfileImageUrl();
        this.dateUpdate = LocalDateTime.now();
    }

    /** 관리자-사원 권한 변경하기 - 관리자 가능 */
    public void putManager() {
        this.isManager = true;
        this.dateUpdate = LocalDateTime.now();
    }

    /** 퇴사처리 및 퇴사일자 변경하기 - 관리자 가능*/
    public void putRetire() {
        this.isWorking = false;
        this.dateRetire = LocalDate.now();
    }

    private Member(MemberBuilder builder) {
        this.isManager = builder.isManager;
        this.employeeNumber = builder.employeeNumber;
        this.name = builder.name;
        this.phone = builder.phone;
        this.birthday = builder.birthday;
        this.gender = builder.gender;
        this.department = builder.department;
        this.position = builder.position;
        this.dateJoin = builder.dateJoin;
        this.isWorking = builder.isWorking;
        this.username = builder.username;
        this.password = builder.password;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;
    }

    public static class MemberBuilder implements CommonModelBuilder<Member> {
        private final Boolean isManager; //관리자 여부
        private final String employeeNumber; //사원번호
        private final String name; //이름
        private final String phone; //연락처
        private final LocalDate birthday; //생년월일
        private final Gender gender; //성별
        private final Department department; //부서
        private final Position position; //직급
        private final LocalDate dateJoin; //입사일
        private final Boolean isWorking; //재직 여부
        private final String username; //사용자 아이디
        private final String password; //사용자 비밀번호
        private final LocalDateTime dateCreate; //등록시간
        private final LocalDateTime dateUpdate; //수정시간

        public MemberBuilder(MemberJoinRequest joinRequest) {
            this.isManager = false;
            this.employeeNumber = joinRequest.getEmployeeNumber();
            this.name = joinRequest.getName();
            this.phone = joinRequest.getPhone();
            this.birthday = joinRequest.getBirthday();
            this.gender = joinRequest.getGender();
            this.department = joinRequest.getDepartment();
            this.position = joinRequest.getPosition();
            this.dateJoin = LocalDate.now();
            this.isWorking = true;
            this.username = joinRequest.getUsername();
            this.password = joinRequest.getPassword();
            this.dateCreate = LocalDateTime.now();
            this.dateUpdate = LocalDateTime.now();
        }

        @Override
        public Member build() {
            return new Member(this);
        }
    }
}
