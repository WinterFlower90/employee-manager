package com.pje.employeemanager.controller;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import com.pje.employeemanager.model.holiday.HolidayRegisterItem;
import com.pje.employeemanager.model.work.WorkDetail;
import com.pje.employeemanager.service.HolidayService;
import com.pje.employeemanager.service.MemberService;
import com.pje.employeemanager.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Api(tags = "휴가 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/V1/holiday")
public class HolidayController {
    private final MemberService memberService;
    private final HolidayService holidayService;

    /** 연차 등록하기 - 관리자용 */
    @ApiOperation(value = "연차 등록하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PostMapping("/set/holiday/manager/{memberId}")
    public CommonResult setHoliday(@PathVariable long memberId, LocalDate dateJoin) {
        holidayService.setHoliday(memberId, dateJoin);
        return ResponseService.getSuccessResult();
    }

    /** 휴가 신청하기 - 사원용 */
    @ApiOperation(value = "휴가 신청하기")
    @PostMapping("/new/holiday")
    public CommonResult setHolidayRegister(Member member, @RequestBody @Valid HolidayApplicationRequest applicationRequest) {
        holidayService.setHolidayRegister(member, applicationRequest);
        return ResponseService.getSuccessResult();
    }

    /** 기간별 휴가 신청 내역 리스트 가져오기 */
    @ApiOperation(value = "기간별 휴가 신청 내역 리스트 가져오기")
    @GetMapping("/holiday/register/search")
    public ListResult<HolidayRegisterItem> getHolidayRegister(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateStart")LocalDate dateStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateEnd")LocalDate dateEnd
    ) {
        return ResponseService.getListResult(holidayService.getHolidayRegister(dateStart, dateEnd), true);
    }

    /** 휴가 신청 상태 변경하기 - 관리자용 */
    @ApiOperation(value = "휴가 신청 상태 변경하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/holiday/register/manager/{memberId}")
    public CommonResult putHolidayStatus(@PathVariable long memberId, HolidayStatus holidayStatus, float increaseOrDecreaseValue) {
        Member member = memberService.getMemberData(memberId);
        holidayService.putHolidayStatus(member, holidayStatus, increaseOrDecreaseValue);
        return ResponseService.getSuccessResult();
    }

    /** 사원 연차 갯수 변경하기(증감) - 관리자용 */
    @ApiOperation(value = "사원 연차 갯수 변경하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/holiday/manager/{memberId}")
    public CommonResult putHolidayCount(@PathVariable long memberId, boolean isMinus, float increaseOrDecreaseValue) {
        Member member = memberService.getMemberData(memberId);
        holidayService.putHolidayCount(member, isMinus, increaseOrDecreaseValue);
        return ResponseService.getSuccessResult();
    }

}
