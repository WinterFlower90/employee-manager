package com.pje.employeemanager.entity;

import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayCount {
    /*
    회원 시퀀스를 HolidayCount의 시퀀스로 쓸 예정.
    회원등록시 같이 휴무갯수 데이터도 생성할 것.
    controller에서 회원등록 후 휴무갯수도 등록하면 된다.
    회원 1명당 휴무갯수 데이터도 1개이기 때문에 회원시퀀스를 이 테이블의 시퀀스로 사용. 그래서 값 자동생성을 하지 않고 수기로 입력.
    이 필드는 PK이기 때문에 joinColumn 쓰는 방식으로 엔티티모양을 끌어올 수 없음. long으로 수기로 넣어줘야함.
    */

    @ApiModelProperty(notes = "시퀀스")
    @Id
    private Long memberId; //자동증가 아님.

    @ApiModelProperty(notes = "총 연차 갯수")
    @Column(nullable = false)
    private Float countTotal; //반차가 0.5개 이기 때문에 실수형으로 설계

    @ApiModelProperty(notes = "사용 연차 갯수")
    @Column(nullable = false)
    private Float countUse; //반차가 0.5개 이기 때문에 실수형으로 설계

    @ApiModelProperty(notes = "연차 갯수 유효 시작일")
    @Column(nullable = false)
    private LocalDate dateHolidayCountStart;

    @ApiModelProperty(notes = "연차 갯수 유효 종료일")
    @Column(nullable = false)
    private LocalDate dateHolidayCountEnd;

    @ApiModelProperty(notes = "등록시간")
    @Column(nullable = false)
    private LocalDateTime dateCreate;

    @ApiModelProperty(notes = "수정시간")
    @Column(nullable = false)
    private LocalDateTime dateUpdate;

    /** 남은 연차 보여주기 */
    public Float getHolidayRemain() {
        return this.countTotal - this.countUse;
    }

    public void putCountUse(float plusValue) {
        this.countUse += plusValue;
        this.dateUpdate = LocalDateTime.now();
    }

    public void putCountTotal(float plusValue) {
        this.countTotal += plusValue;
        this.dateUpdate = LocalDateTime.now();
    }

    public void plusCountTotal(float plusCount) {
        this.countTotal += plusCount;
    }

    public void plusCountUse(float plusCount) {
        this.countUse += plusCount;
    }

    public void minusCountTotal(float minusCount) {
        this.countTotal -= minusCount;
    }

    public void minusCountUse(float minusCount) {
        this.countUse -= minusCount;
    }

    private HolidayCount(HolidayCountBuilder builder) {
        this.memberId = builder.memberId;
        this.countTotal = builder.countTotal;
        this.countUse = builder.countUse;
        this.dateHolidayCountStart = builder.dateHolidayCountStart;
        this.dateHolidayCountEnd = builder.dateHolidayCountEnd;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;
    }

    public static class HolidayCountBuilder implements CommonModelBuilder<HolidayCount> {
        private final Long memberId;
        private final Float countTotal;
        private final Float countUse;
        private final LocalDate dateHolidayCountStart;
        private final LocalDate dateHolidayCountEnd;
        private final LocalDateTime dateCreate;
        private final LocalDateTime dateUpdate;

        //연차갯수 최초 등록시 회원 시퀀스, 입사일을 받아야 한다.
        public HolidayCountBuilder(Long memberId, LocalDate dateJoin) {
            this.memberId = memberId;
            this.countTotal = 0f; //최초 입사시 연차가 없기 때문에 0
            this.countUse = 0f; //최초 입사시 사용연차가 없기 때문에 0
            this.dateHolidayCountStart = dateJoin; //입사일 기준으로 연차 생성 시작.
            this.dateHolidayCountEnd = dateJoin.plusYears(1); //입사일 기준 1년이 유효기간. (노동법 참고)
            this.dateCreate = LocalDateTime.now();
            this.dateUpdate = LocalDateTime.now();
        }

        @Override
        public HolidayCount build() {
            return new HolidayCount(this);
        }
    }
}
