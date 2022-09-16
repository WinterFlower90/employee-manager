package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkDetail {
    private Long workId;

    private Long memberId;

    private String memberFullName;

    private LocalDate dateWork;

    private String workStatus;

    private LocalTime inWork;

    private LocalTime pauseWork;

    private LocalTime returnWork;

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
