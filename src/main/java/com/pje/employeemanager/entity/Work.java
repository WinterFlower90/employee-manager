package com.pje.employeemanager.entity;

import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import com.pje.employeemanager.model.work.AttendanceRequest;
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
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private WorkStatus workStatus; //상태

    @Column(nullable = false)
    private LocalDate dateWork; //근무 일자

    @Column(nullable = false)
    private LocalTime inWork; //출근 시간

    @Column(nullable = true)
    private LocalTime pauseWork; //외출 시간

    @Column(nullable = true)
    private LocalTime returnWork; //복귀 시간

    @Column(nullable = true)
    private LocalTime outWork; //퇴근 시간

    @Column(nullable = false)
    private LocalDateTime dateCreate; //등록 시간

    @Column(nullable = false)
    private LocalDateTime dateUpdate; //수정 시간

    private Work(WorkBuilder builder) {
        this.member = builder.member;
        this.workStatus = builder.workStatus;
        this.dateWork = builder.dateWork;
        this.inWork = builder.inWork;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;

    }

    public static class WorkBuilder implements CommonModelBuilder<Work> {
        private Member member;
        private WorkStatus workStatus;
        private LocalDate dateWork;
        private LocalTime inWork;
        private LocalDateTime dateCreate;
        private LocalDateTime dateUpdate;

        public WorkBuilder(Member member, WorkStatus workStatus) {
            this.member = member;
            this.workStatus = workStatus;
            this.dateWork = LocalDate.now();
            this.inWork = LocalTime.now();
            this.dateCreate = LocalDateTime.now();
            this.dateUpdate = LocalDateTime.now();
        }

        @Override
        public Work build() {
            return new Work(this);
        }
    }
}
