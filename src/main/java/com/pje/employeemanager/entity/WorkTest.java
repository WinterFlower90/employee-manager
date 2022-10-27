package com.pje.employeemanager.entity;

import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 출근회원
    @Column(nullable = false)
    private Long memberId;

    // 출근일
    @Column(nullable = false)
    private LocalDate dateWork;

    // 출근상태
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 15)
    private WorkStatus workStatus;

    // 출근시간
    @Column(nullable = false)
    private LocalTime inWork;

    private LocalTime pauseWork; //외출 시간

    private LocalTime returnWork; //복귀 시간

    private LocalTime earlyLeaveWork; //조퇴 시간

    private LocalTime outWork; //퇴근 시간

    private LocalDateTime dateCreate;

    public void putStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;

        switch (workStatus) {
            case EARLY_LEAVE:
                this.earlyLeaveWork = LocalTime.now();
                break;
            case LEAVE_WORK:
                this.outWork = LocalTime.now();
        }
    }

    private WorkTest(WorkTestBuilder builder) {
        this.memberId = builder.memberId;
        this.dateWork = builder.dateWork;
        this.workStatus = builder.workStatus;
        this.inWork = builder.inWork;
        this.dateCreate = builder.dateCreate;
    }

    public static class WorkTestBuilder implements CommonModelBuilder<WorkTest> {
        private final Long memberId;
        private final LocalDate dateWork;
        private final WorkStatus workStatus;
        private final LocalTime inWork;
        private final LocalDateTime dateCreate;

        public WorkTestBuilder(Long memberId) {
            this.memberId = memberId;
            this.dateWork = LocalDate.now();
            this.workStatus = WorkStatus.ATTENDANCE;
            this.inWork = LocalTime.now();
            this.dateCreate = LocalDateTime.now();
        }

        @Override
        public WorkTest build() {
            return new WorkTest(this);
        }
    }

}
