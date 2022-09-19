package com.pje.employeemanager.controller;

import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.service.HolidayService;
import com.pje.employeemanager.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Api(tags = "휴가 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/V1/holiday")
public class HolidayController {
    private final HolidayService holidayService;

    @ApiOperation(value = "연차 등록하기")
    @PostMapping("/set-holiday/{memberId}")
    public CommonResult setHoliday(@PathVariable long memberId, LocalDate dateJoin) {
        holidayService.setHoliday(memberId, dateJoin);
        return ResponseService.getSuccessResult();
    }


}
