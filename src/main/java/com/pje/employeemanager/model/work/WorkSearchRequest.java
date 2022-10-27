package com.pje.employeemanager.model.work;

import com.pje.employeemanager.enums.WorkStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
public class WorkSearchRequest {
    /** 검색 필터 설계하기.
     아무 필터도 설정하지 않으면 전체 리스트가 나오는것이 포인트 */

    @ApiModelProperty(notes = "사원 아이디")
    private String username; // like 검색

    @ApiModelProperty(notes = "사원 이름")
    private String name;  // like 검색

    @ApiModelProperty(notes = "근무 상태")
    @Enumerated(value = EnumType.STRING)
    private WorkStatus workStatus; //상태. 일치하는것 검색

    @ApiModelProperty(notes = "근무일 (검색 시작일)")
    private LocalDate dateWorkStart; //근무 일자

    @ApiModelProperty(notes = "근무일 (검색 종료일)")
    private LocalDate dateWorkEnd; //근무 일자

}
