package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.entity.WorkTest;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkTestDetail {
    @ApiModelProperty(notes = "근무 시퀀스")
    private Long workId;

    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "근무 일자")
    private LocalDate dateWork;

    @ApiModelProperty(notes = "근무 상태")
    private String workStatus;

    @ApiModelProperty(notes = "출근 시간")
    private LocalTime inWork;

    @ApiModelProperty(notes = "외출 시간", required = false)
    private LocalTime pauseWork;

    @ApiModelProperty(notes = "복귀 시간", required = false)
    private LocalTime returnWork;

    @ApiModelProperty(notes = "퇴근 시간", required = false)
    private LocalTime outWork;

    @ApiModelProperty(notes = "데이터 생성시간")
    private LocalDateTime dateCreate;

    private WorkTestDetail(WorkDetailBuilder builder) {
        this.workId = builder.workId;
        this.memberId = builder.memberId;
        this.dateWork = builder.dateWork;
        this.workStatus = builder.workStatus;
        this.inWork = builder.inWork;
        this.pauseWork = builder.pauseWork;
        this.returnWork = builder.returnWork;
        this.outWork = builder.outWork;
        this.dateCreate = builder.dateCreate;
    }


    public static class WorkDetailBuilder implements CommonModelBuilder<WorkTestDetail> {
        private final Long workId;
        private final Long memberId;
        private final LocalDate dateWork;
        private final String workStatus;
        private final LocalTime inWork;
        private final LocalTime pauseWork;
        private final LocalTime returnWork;
        private final LocalTime outWork;
        private final LocalDateTime dateCreate;

        public WorkDetailBuilder(WorkTest workTest) {
            this.workId = workTest.getId();
            this.memberId = workTest.getMemberId();
            this.dateWork = workTest.getDateWork();
            this.workStatus = workTest.getWorkStatus().getName();
            this.inWork = workTest.getInWork();
            this.pauseWork = workTest.getPauseWork();
            this.returnWork = workTest.getReturnWork();
            this.outWork = workTest.getOutWork();
            this.dateCreate = workTest.getDateCreate();
        }

        @Override
        public WorkTestDetail build() {
            return new WorkTestDetail(this);
        }
    }
}
