package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkDetail {
    @ApiModelProperty(notes = "근무 시퀀스")
    private Long workId;

    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "[부서] 사원 이름 + 사원 직급")
    private String memberFullName;

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

    private WorkDetail(WorkDetailBuilder builder) {
        this.workId = builder.workId;
        this.memberId = builder.memberId;
        this.memberFullName = builder.memberFullName;
        this.dateWork = builder.dateWork;
        this.workStatus = builder.workStatus;
        this.inWork = builder.inWork;
        this.pauseWork = builder.pauseWork;
        this.returnWork = builder.returnWork;
        this.outWork = builder.outWork;
    }

    public static class WorkDetailBuilder implements CommonModelBuilder<WorkDetail> {
        private final Long workId;
        private final Long memberId;
        private final String memberFullName;
        private final LocalDate dateWork;
        private final String workStatus;
        private final LocalTime inWork;
        private final LocalTime pauseWork;
        private final LocalTime returnWork;
        private final LocalTime outWork;

        public WorkDetailBuilder(Work work) {
            this.workId = work.getId();
            this.memberId = work.getMember().getId();
            this.memberFullName = "[" + work.getMember().getDepartment().getName() + "] " + work.getMember().getName() + " " + work.getMember().getPosition().getName();
            this.dateWork = work.getDateWork();
            this.workStatus = work.getWorkStatus().getName();
            this.inWork = work.getInWork();
            this.pauseWork = work.getPauseWork();
            this.returnWork = work.getReturnWork();
            this.outWork = work.getOutWork();
        }

        @Override
        public WorkDetail build() {
            return new WorkDetail(this);
        }
    }
}
