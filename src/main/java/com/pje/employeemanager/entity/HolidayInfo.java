package com.pje.employeemanager.entity;

import com.pje.employeemanager.interfaces.CommonModelBuilder;
import com.pje.employeemanager.model.holiday.HolidayInfoRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Integer holidayTotal; //총 연차 갯수

    @Column(nullable = false)
    private Float holidayUse; //사용 연차

    @Column(nullable = false)
    private Float holidayRemain; //남은 연차 갯수

    @Column(nullable = false)
    private LocalDateTime dateCreate; //생성 일자

    @Column(nullable = false)
    private LocalDateTime dateUpdate; //수정 일자



    private HolidayInfo(HolidayInfoBuilder builder) {
        this.member = builder.member;
        this.holidayTotal = builder.holidayTotal;
        this.holidayUse = builder.holidayUse;
        this.holidayRemain = builder.holidayRemain;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;
    }

    public static class HolidayInfoBuilder implements CommonModelBuilder<HolidayInfo> {
        private final Member member;
        private final Integer holidayTotal;
        private final Float holidayUse;
        private final Float holidayRemain;
        private final LocalDateTime dateCreate;
        private final LocalDateTime dateUpdate;

        public HolidayInfoBuilder(Member member, HolidayInfoRequest infoRequest) {
            this.member = member;
            this.holidayTotal = infoRequest.getHolidayTotal();
            this.holidayUse = infoRequest.getHolidayUse();
            this.holidayRemain = infoRequest.getHolidayRemain();
            this.dateCreate = LocalDateTime.now();
            this.dateUpdate = LocalDateTime.now();
        }

        @Override
        public HolidayInfo build() {
            return new HolidayInfo(this);
        }
    }
}
