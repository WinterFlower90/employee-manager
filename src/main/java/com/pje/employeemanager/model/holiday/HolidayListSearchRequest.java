package com.pje.employeemanager.model.holiday;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class HolidayListSearchRequest {
    @ApiModelProperty(notes = "회원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "검색 시작일")
    @NotNull
    private LocalDate dateStart;

    @ApiModelProperty(notes = "검색 종료일")
    @NotNull
    private LocalDate dateEnd;

}
