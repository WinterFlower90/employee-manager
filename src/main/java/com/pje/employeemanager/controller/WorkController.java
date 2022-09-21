package com.pje.employeemanager.controller;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.SingleResult;
import com.pje.employeemanager.model.work.*;
import com.pje.employeemanager.service.WorkService;
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

@Api(tags = "근무 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/V1/work")
public class WorkController {
    private final MemberService memberService;
    private final WorkService workService;

    @ApiOperation(value = "근무 정보 등록하기")
    @PostMapping("/new")
    public CommonResult setWork(Member member) {
        workService.setWork(member);
        return ResponseService.getSuccessResult();
    }

    @ApiOperation(value = "기간별 출근 내역 리스트 가져오기")
    @GetMapping("/work/search")
    public ListResult<WorkDetail> getWorkDetails(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateStart")LocalDate dateStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateEnd")LocalDate dateEnd
            ) {
        return ResponseService.getListResult(workService.getWorkDetails(dateStart, dateEnd), true);
    }

    @ApiOperation(value = "사원별 출근 내역 리스트 가져오기")
    @GetMapping("/work/member/search/{memberId}")
    public ListResult<WorkDetail> getMemberWorkDetails(@PathVariable long memberId) {
        return ResponseService.getListResult(workService.getMemberWorkDetails(memberId), true);
    }

    @ApiOperation(value = "나의 근태 상태 가져오기")
    @GetMapping("/my/status/{memberId}")
    public SingleResult<WorkStatusResponse> getMyStatus(@PathVariable long memberId) {
        return ResponseService.getSingleResult(workService.getMyStatus(memberId));
    }

    @ApiOperation(value = "출근 처리하기")
    @PostMapping("/company-in")
    public CommonResult setStatusCompanyIn(Member member) {
        workService.setStatusCompanyIn(member);
        return ResponseService.getSuccessResult();
    }

    @ApiOperation(value = "출근 시간 변경하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workId", value = "근무 시퀀스", required = true)
    })
    @PutMapping("/time-reset/{workId}")
    public CommonResult putWorkTime(Member member, @PathVariable long workId, @RequestBody @Valid WorkTimeResetRequest resetRequest) {
        workService.putWorkTime(workId, member, resetRequest);
        return ResponseService.getSuccessResult();
    }

    /** 나의 근태상태 수정하기 - 사원용 */
    @ApiOperation(value = "나의 근태상태 수정하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workId", value = "근무 시퀀스", required = true),
            @ApiImplicitParam(name = "workStatus", value = "근무 상태", required = true)
    })
    @PutMapping("/my/{memberId}/status/{workStatus}")
    public CommonResult putStatus(@PathVariable long memberId, @PathVariable WorkStatus workStatus) {
        Member member = memberService.getMemberData(memberId);
        workService.putStatus(workStatus, member);
        return ResponseService.getSuccessResult();
    }

    /** 관리자용 근무 리스트 가져오기 (필터기능 o) */
    @ApiOperation(value = "관리자용 근무 리스트 가져오기")
    @PostMapping("/works/page/{pageNum}")
    public ListResult<WorkAdminListItem> getWorkListByAdmin(@PathVariable int pageNum, @RequestBody @Valid WorkSearchRequest workSearchRequest) {
        return ResponseService.getListResult(workService.getList(pageNum, workSearchRequest), true);
    }
}
