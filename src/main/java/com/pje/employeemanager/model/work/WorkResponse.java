package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkResponse {
    //출근/조퇴/퇴근...
    private String workStatusName;

    //enum key값
    private String workStatus;

    private WorkResponse(WorkResponseBuilder builder) {
        this.workStatusName = builder.workStatusName;
        this.workStatus = builder.workStatus;
    }

    private WorkResponse(WorkResponseNoneBuilder builder) {
        this.workStatusName = builder.workStatusName;
        this.workStatus = builder.workStatus;
    }

    public static class WorkResponseBuilder implements CommonModelBuilder<WorkResponse> {
        private final String workStatusName;
        private final String workStatus;

        public WorkResponseBuilder(Work work) {
            this.workStatusName = work.getWorkStatus().getName();
            this.workStatus = work.getWorkStatus().toString();
        }

        @Override
        public WorkResponse build() {
            return new WorkResponse(this);
        }
    }


    public static class WorkResponseNoneBuilder implements CommonModelBuilder<WorkResponse> {
        private final String workStatusName;
        private final String workStatus;

        public WorkResponseNoneBuilder() {
            this.workStatusName = WorkStatus.NO_STATUS.getName();
            this.workStatus = WorkStatus.NO_STATUS.toString();
        }

        @Override
        public WorkResponse build() {
            return new WorkResponse(this);
        }
    }
}
