package com.pje.employeemanager.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TestHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "대상 직원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ApiModelProperty(notes = "연차 사용일")
    @Column(nullable = false)
    private LocalDate dateHolidayRequest;

    @ApiModelProperty(notes = "승인여부 (true : 승인, false : 미승인)")
    @Column(nullable = false)
    private Boolean isComplete;
}

