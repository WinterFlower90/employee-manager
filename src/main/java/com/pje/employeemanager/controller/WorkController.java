package com.pje.employeemanager.controller;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.SingleResult;
import com.pje.employeemanager.model.work.WorkDetail;
import com.pje.employeemanager.model.work.WorkStatusResponse;
import com.pje.employeemanager.service.WorkService;
import com.pje.employeemanager.service.MemberService;
import com.pje.employeemanager.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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
            @DateTimeFormat(pattern = "yyyy-MM") @RequestParam(value = "dateStart")LocalDate dateStart,
            @DateTimeFormat(pattern = "yyyy-MM") @RequestParam(value = "dateEnd")LocalDate dateEnd
            ) {
        return ResponseService.getListResult(workService.getWorkDetails(dateStart, dateEnd), true);
    }

    @ApiOperation(value = "사원별 출근 내역 리스트 가져오기")
    @GetMapping("/work/member/search")
    public ListResult<WorkDetail> getMemberWorkDetails(@PathVariable long memberId) {
        return ResponseService.getListResult(workService.getMemberWorkDetails(memberId), true);
    }

    @ApiOperation(value = "나의 근태 상태 가져오기")
    @GetMapping("/work/member/{memberId}")
    public SingleResult<WorkStatusResponse> getMyStatus(@PathVariable long memberId) {
        return ResponseService.getSingleResult(workService.getMyStatus(memberId));
    }

    @ApiOperation(value = "출근 처리하기")
    @PostMapping("/company-in")
    public CommonResult setStatusCompanyIn(Member member) {
        workService.setStatusCompanyIn(member);
        return ResponseService.getSuccessResult();
    }

    /** 수정해야함.. */
    @ApiOperation(value = "dd")
    @PutMapping("/")
    public CommonResult putStatus(WorkStatus workStatus, Member member) {
        workService.putStatus(workStatus, member);
        return ResponseService.getSuccessResult();
    }
}
