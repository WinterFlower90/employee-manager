package com.pje.employeemanager.entity;

import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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



    private HolidayHistory(HolidayInfoBuilder builder) {
        this.member = builder.member;
        this.isMinus = builder.isMinus;
        this.increaseOrDecreaseValue = builder.increaseOrDecreaseValue;
        this.dateCreate = builder.dateCreate;
        this.dateUpdate = builder.dateUpdate;
    }

    public static class HolidayInfoBuilder implements CommonModelBuilder<HolidayHistory> {
        private final Member member;
        private final Boolean isMinus;
        private final Float increaseOrDecreaseValue;
        private final LocalDateTime dateCreate;
        private final LocalDateTime dateUpdate;

        public HolidayInfoBuilder(Member member, Boolean isMinus, Float increaseOrDecreaseValue) {
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
}
