package com.pje.employeemanager.entity;

import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.enums.HolidayType;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import com.pje.employeemanager.model.holiday.HolidayStatusRequest;
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

    @Column(nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private HolidayStatus holidayStatus; //승인 여부 - 검토중 / 승인 / 반려

    @Column(nullable = true)
    private LocalDateTime dateApproval; //승인 시간 (update 개념)

    /*
    @Column(nullable = true)
    private LocalDateTime dateRefusal; //반려 시간 (update 개념)
     */

    /*
    public void putHolidayStatus(HolidayStatus holidayStatus) {
        this.holidayStatus = holidayStatus;

        switch (holidayStatus) {
            case OK -> this.dateApproval = LocalDateTime.now();
            case CANCEL -> this.dateRefusal = LocalDateTime.now();
        }
    }
     */
    /** 휴가 승인 상태 변경하기 */
    public void  putHolidayStatus(HolidayStatusRequest holidayStatusRequest) {
        this.holidayStatus = holidayStatusRequest.getHolidayStatus();
        this.dateApproval = LocalDateTime.now();
    }

    private HolidayRegister(HolidayBuilder builder) {
        this.member = builder.member;
        this.holidayType = builder.holidayType;
        this.reason = builder.reason;
        this.dateDesired = builder.dateDesired;
        this.dateApplication = builder.dateApplication;
        this.holidayStatus = builder.holidayStatus;
    }

    public static class HolidayBuilder implements CommonModelBuilder<HolidayRegister> {
        private final Member member;
        private final HolidayType holidayType;
        private final String reason;
        private final LocalDate dateDesired;
        private final LocalDate dateApplication;
        private final HolidayStatus holidayStatus;

        public HolidayBuilder(Member member, HolidayApplicationRequest request) {
            this.member = member;
            this.holidayType = request.getHolidayType();
            this.reason = request.getReason();
            this.dateDesired = request.getDateDesired();
            this.dateApplication = LocalDate.now();
            this.holidayStatus = HolidayStatus.NO_STATUS;
        }
        @Override
        public HolidayRegister build() {
            return new HolidayRegister(this);
        }
    }
}
