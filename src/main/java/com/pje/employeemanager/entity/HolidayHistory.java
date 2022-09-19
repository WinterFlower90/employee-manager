package com.pje.employeemanager.entity;

import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.enums.HolidayType;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import com.pje.employeemanager.model.holiday.HolidayStatusRequest;
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
public class HolidayHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    /** 연차 갯수를 줄 수도 있기 때문에 증감 여부로 설계 */
    @ApiModelProperty(notes = "차감여부 (true 차감, false 증가)")
    @Column(nullable = false)
    private Boolean isMinus;

    @ApiModelProperty(notes = "증감값")
    @Column(nullable = false)
    private Float increaseOrDecreaseValue;

    @Column(nullable = false)
    private LocalDateTime dateCreate; //생성 일자

    @Column(nullable = false)
    private LocalDateTime dateUpdate; //수정 일자

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

    @Column(nullable = true)
    private LocalDateTime dateRefusal; //반려 시간 (update 개념)


    private HolidayHistory(HolidayHistoryBuilder builder) {
        this.member = builder.member;
        this.isMinus = builder.isMinus;
        this.increaseOrDecreaseValue = builder.increaseOrDecreaseValue;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;
    }

    public static class HolidayHistoryBuilder implements CommonModelBuilder<HolidayHistory> {
        private final Member member;
        private final Boolean isMinus;
        private final Float increaseOrDecreaseValue;
        private final LocalDateTime dateCreate;
        private final LocalDateTime dateUpdate;

        public HolidayHistoryBuilder(Member member, Boolean isMinus, Float increaseOrDecreaseValue) {
            this.member = member;
            this.isMinus = isMinus;
            this.increaseOrDecreaseValue = increaseOrDecreaseValue;
            this.dateCreate = LocalDateTime.now();
            this.dateUpdate = LocalDateTime.now();
        }

        @Override
        public HolidayHistory build() {
            return new HolidayHistory(this);
        }
    }

    /** 휴가 승인 상태로 변경하기 */
    public void  putHolidayApproval(HolidayStatus holidayStatus) {
        this.holidayStatus = HolidayStatus.OK;
        this.dateApproval = LocalDateTime.now();
    }

    /** 휴가 반려 상태로 변경하기 */
    public void putHolidayRefusal(HolidayStatus holidayStatus) {
        this.holidayStatus = HolidayStatus.CANCEL;
        this.dateRefusal = LocalDateTime.now();
    }

    /** 사원이 반차나 연차를 신청 후 관리자가 승인했을때 차감시키기 위한 빌더패턴. */
    private HolidayHistory (HolidayRegisterBuilder registerBuilder) {
        this.member = registerBuilder.member;
        this.holidayType = registerBuilder.holidayType;
        this.reason = registerBuilder.reason;
        this.dateDesired = registerBuilder.dateDesired;
        this.dateApplication = registerBuilder.dateApplication;
        this.holidayStatus = registerBuilder.holidayStatus;
    }
    public static class HolidayRegisterBuilder implements CommonModelBuilder<HolidayHistory> {
        private final Member member;
        private final HolidayType holidayType;
        private final String reason;
        private final LocalDate dateDesired;
        private final LocalDate dateApplication;
        private final HolidayStatus holidayStatus; //승인 여부 - 검토중 / 승인 / 반려

        public HolidayRegisterBuilder(Member member, HolidayApplicationRequest applicationRequest) {
            this.member = member;
            this.holidayType = applicationRequest.getHolidayType();
            this.reason = applicationRequest.getReason();
            this.dateDesired = applicationRequest.getDateDesired();
            this.dateApplication = LocalDate.now();
            this.holidayStatus = HolidayStatus.NO_STATUS;
        }
        @Override
        public HolidayHistory build() {
            return new HolidayHistory(this);
        }
    }
}
