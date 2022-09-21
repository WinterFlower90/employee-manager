package com.pje.employeemanager.model.member;

import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.enums.Gender;
import com.pje.employeemanager.enums.Position;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
public class MemberSearchRequest {
    /** 검색 필터 설계하기.
     아무 필터도 설정하지 않으면 전체 리스트가 나오는것이 포인트 */

    @ApiModelProperty(notes = "사원 아이디")
    private String username; // like 검색

    @ApiModelProperty(notes = "사원 이름")
    private String name;  // like 검색

    @ApiModelProperty(notes = "사원 연락처")
    private String phone;  // like 검색

    @ApiModelProperty(notes = "사원 성별")
    @Enumerated(value = EnumType.STRING)
    private Gender gender; // 일치하는 것 검색

    @ApiModelProperty(notes = "사원 부서")
    @Enumerated(value = EnumType.STRING)
    private Department department;  // 일치하는 것 검색

    @ApiModelProperty(notes = "사원 직급")
    @Enumerated(value = EnumType.STRING)
    private Position position;  // 일치하는 것 검색

    @ApiModelProperty(notes = "사원 재직(활성) 여부")
    private Boolean isWorking;  // 일치하는 것 검색

    @ApiModelProperty(notes = "관리자 여부")
    private Boolean isManager;  // 일치하는 것 검색

    @ApiModelProperty(notes = "입사일 (검색 시작일)")
    private LocalDate dateJoinStart;

    @ApiModelProperty(notes = "입사일 (검색 종료일)")
    private LocalDate dateJoinEnd;
}
