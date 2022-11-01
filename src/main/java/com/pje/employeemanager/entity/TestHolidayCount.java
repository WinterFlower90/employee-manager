package com.pje.employeemanager.entity;

import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestHolidayCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "대상 직원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ApiModelProperty(notes = "연차 기준 시작일")
    @Column(nullable = false)
    private LocalDate dateStart;

    @ApiModelProperty(notes = "연차 기준 종료일")
    @Column(nullable = false)
    private LocalDate dateEnd;

    @ApiModelProperty(notes = "총 연차 개수")
    @Column(nullable = false)
    private Integer countTotal;

    @ApiModelProperty(notes = "등록시간")
    @Column(nullable = false)
    private LocalDateTime dateCreate;

    @ApiModelProperty(notes = "수정시간")
    @Column(nullable = false)
    private LocalDateTime dateUpdate;

    private TestHolidayCount(TestHolidayCountBuilder builder) {
        this.member = builder.member;
        this.dateStart = builder.dateStart;
        this.dateEnd = builder.dateEnd;
        this.countTotal = builder.countTotal;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;
    }

    public static class TestHolidayCountBuilder implements CommonModelBuilder<TestHolidayCount> {
        private final Member member;
        private final LocalDate dateStart;
        private final LocalDate dateEnd;
        private final Integer countTotal;
        private final LocalDateTime dateCreate;
        private final LocalDateTime dateUpdate;

        public TestHolidayCountBuilder(Member member, LocalDate dateStart) {
            this.member = member;
            this.dateStart = dateStart;

            // 연차 기준 종료일은 시작일 + 1년 - 1일
            // 예) 2022.01.01 ~ 2022.12.31
            this.dateEnd = dateStart.plusYears(1).minusDays(1);

            // 최초 등록시 0개로 세팅.
            this.countTotal = 0;
            this.dateCreate = LocalDateTime.now();
            this.dateUpdate = LocalDateTime.now();
        }

        @Override
        public TestHolidayCount build() {
            return new TestHolidayCount(this);
        }
    }
}

