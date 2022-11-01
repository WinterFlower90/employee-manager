package com.pje.employeemanager.controller;

import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.SingleResult;
import com.pje.employeemanager.model.holiday.*;
import com.pje.employeemanager.model.work.WorkAdminListItem;
import com.pje.employeemanager.model.work.WorkDetail;
import com.pje.employeemanager.model.work.WorkSearchRequest;
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
    @PostMapping("/set/manager/{memberId}")
    public CommonResult setHolidayCount(@PathVariable long memberId, LocalDate dateJoin) {
        holidayService.setHoliday(memberId, dateJoin);
        return ResponseService.getSuccessResult();
    }

    /** 휴가 신청하기 - 사원용 */
    @ApiOperation(value = "휴가 신청하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PostMapping("/new/my/{memberId}")
    public CommonResult setHolidayCreate(@PathVariable long memberId, @RequestBody @Valid HolidayCreateRequest createRequest) {
        holidayService.setHolidayCreate(memberService.getMemberData(memberId), createRequest);
        return ResponseService.getSuccessResult();
    }

    @ApiOperation(value = "전체 사원의 휴가 신청 내역 리스트 가져오기")
    @GetMapping("/search")
    public ListResult<HolidayRegisterItem> getHolidayRegister() {
        return ResponseService.getListResult(holidayService.getHolidayRegister(), true);
    }

    /** 휴가 승인 상태 변경하기 - 관리자용 */
    @ApiOperation(value = "휴가 승인 상태 변경하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "holidayHistoryId", value = "휴가 기록 시퀀스", required = true)
    })
    @PutMapping("/register/manager/{holidayHistoryId}")
    public CommonResult putHolidayStatus(@PathVariable long holidayHistoryId, @RequestBody @Valid HolidayStatusRequest holidayStatusRequest) {
        holidayService.putHolidayStatus(holidayHistoryId, holidayStatusRequest);
        return ResponseService.getSuccessResult();

    }

    @ApiOperation(value = "사원별 연차 사용현황 갯수 리스트 가져오기")
    @GetMapping("/member/all")
    public ListResult<HolidayCountListItem> getHolidayCounts() {
        return ResponseService.getListResult(holidayService.getHolidayCounts(), true);
    }

    /** 사원 연차 갯수 변경하기(증감) - 관리자용 */
    @ApiOperation(value = "사원 연차 갯수 변경하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/manager/{memberId}")
    public CommonResult putHolidayCount(@PathVariable long memberId, @RequestBody @Valid HolidayCountRequest holidayCountRequest) {
        Member member = memberService.getMemberData(memberId);
        holidayService.putHolidayCount(member, holidayCountRequest);
        return ResponseService.getSuccessResult();
    }

    /** 관리자용 휴가 신청 리스트 가져오기 (필터기능 o) */
    @ApiOperation(value = "관리자용 휴가 신청 리스트 가져오기")
    @PostMapping("/page/{pageNum}")
    public ListResult<HolidayAdminListItem> getHolidayListByAdmin(@PathVariable int pageNum, @RequestBody @Valid HolidaySearchRequest holidaySearchRequest) {
        return ResponseService.getListResult(holidayService.getList(pageNum, holidaySearchRequest), true);
    }

    /** 관리자용 기간별 or 특정 사원 휴가 신청 리스트 가져오기 */
    @ApiOperation(value = "관리자용 기간별 휴가 신청 리스트 가져오기")
    @PostMapping("/period/page/{pageNum}")
    public ListResult<HolidayAdminListItem> getHolidayListByAdmin(@PathVariable int pageNum, @RequestBody @Valid HolidayListSearchRequest request) {
        return ResponseService.getListResult(holidayService.getListByAdmin(pageNum, request), true);
    }

    @ApiOperation(value = "연차 초기값 등록하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true),
            @ApiImplicitParam(name = "dateCriteria", value = "조회 기준일", required = true),
    })
    @PostMapping("/create/member-id/{memberId}")
    public CommonResult setDefaultCount(
            @PathVariable long memberId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateCriteria")LocalDate dateCriteria) {
        holidayService.setDefaultCount(memberService.getMemberData(memberId), dateCriteria);
        return ResponseService.getSuccessResult();
    }

    @ApiOperation(value = "연차 갯수 정보 조회하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true),
            @ApiImplicitParam(name = "dateCriteria", value = "조회 기준일", required = true)
    })
    @GetMapping("/count/search/member-id/{memberId}")
    public SingleResult<MyHolidayCountResponse> getMyCount(
            @PathVariable long memberId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateCriteria")LocalDate dateCriteria) {
        return ResponseService.getSingleResult(holidayService.getMyCount(memberService.getMemberData(memberId), dateCriteria));
    }



    //    /** 기간별 휴가 신청 내역 리스트 가져오기 */
//    @ApiOperation(value = "기간별 휴가 신청 내역 리스트 가져오기")
//    @GetMapping("/holiday/register/search")
//    public ListResult<HolidayRegisterItem> getHolidayRegister(
//            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateStart")LocalDate dateStart,
//            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateEnd")LocalDate dateEnd
//    ) {
//        return ResponseService.getListResult(holidayService.getHolidayRegister(dateStart, dateEnd), true);
//    }

}
