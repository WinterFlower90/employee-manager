package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkStatusResponse {
    @ApiModelProperty(value = "근태 상태")
    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    private WorkStatusResponse(WorkStatusResponseBuilder builder) {
        this.workStatus = builder.workStatus;
    }

    public static class WorkStatusResponseBuilder implements CommonModelBuilder<WorkStatusResponse> {
        private final WorkStatus workStatus;

        public WorkStatusResponseBuilder(Work work) {
            this.workStatus = work.getWorkStatus();
        }


        @Override
        public WorkStatusResponse build() {
            return new WorkStatusResponse(this);
        }
    }
}
