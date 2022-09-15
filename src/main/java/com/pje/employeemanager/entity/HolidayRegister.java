package com.pje.employeemanager.entity;

import com.pje.employeemanager.enums.HolidayType;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private HolidayType holidayType; //휴가 타입 : 연차, 병가 ...

    @Column(nullable = false)
    private String reason; //사유

    @Column(nullable = false)
    private LocalDate dateDesired; //희망 일자

    @Column(nullable = false)
    private LocalDate dateApplication; //신청 일자

    @Column(nullable = false)
    private Boolean isApproval; //승인 여부 - 기본 false

    @Column(nullable = true)
    private LocalDateTime dateApproval; //승인 시간 (update 개념)

    private HolidayRegister(HolidayBuilder builder) {
        this.member = builder.member;
        this.holidayType = builder.holidayType;
        this.reason = builder.reason;
        this.dateDesired = builder.dateDesired;
        this.dateApplication = builder.dateApplication;
        this.isApproval = builder.isApproval;
    }

    public static class HolidayBuilder implements CommonModelBuilder<HolidayRegister> {
        private final Member member;
        private final HolidayType holidayType;
        private final String reason;
        private final LocalDate dateDesired;
        private final LocalDate dateApplication;
        private final Boolean isApproval;

        public HolidayBuilder(Member member, HolidayApplicationRequest request) {
            this.member = member;
            this.holidayType = request.getHolidayType();
            this.reason = request.getReason();
            this.dateDesired = request.getDateDesired();
            this.dateApplication = LocalDate.now();
            this.isApproval = false;
        }
        @Override
        public HolidayRegister build() {
            return new HolidayRegister(this);
        }
    }
}
