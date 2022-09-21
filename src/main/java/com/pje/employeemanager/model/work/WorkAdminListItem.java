package com.pje.employeemanager.model.work;

import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkAdminListItem {
    @ApiModelProperty(notes = "사원 시퀀스")
    private Long memberId;

    @ApiModelProperty(notes = "근무 시퀀스")
    private Long workId;

    @ApiModelProperty(notes = "사원 아이디")
    private String username;

    @ApiModelProperty(notes = "사원 이름")
    private String name;

    @ApiModelProperty(notes = "근무 상태")
    private String workStatus;

    @ApiModelProperty(notes = "근무 날짜")
    private LocalDate dateWork;

    private WorkAdminListItem(WorkAdminListItemBuilder builder) {
        this.memberId = builder.memberId;
        this.workId = builder.workId;
        this.username = builder.username;
        this.name = builder.name;
        this.workStatus = builder.workStatus;
        this.dateWork = builder.dateWork;
    }

    public static class WorkAdminListItemBuilder implements CommonModelBuilder<WorkAdminListItem> {
        private final Long memberId;
        private final Long workId;
        private final String username;
        private final String name;
        private final String workStatus;
        private final LocalDate dateWork;

        public WorkAdminListItemBuilder(Work work) {
            this.memberId = work.getMember().getId();
            this.workId = work.getId();
            this.username = work.getMember().getUsername();
            this.name = work.getMember().getName();
            this.workStatus = work.getWorkStatus().getName();
            this.dateWork = work.getDateWork();
        }

        @Override
        public WorkAdminListItem build() {
            return new WorkAdminListItem(this);
        }
    }
}
