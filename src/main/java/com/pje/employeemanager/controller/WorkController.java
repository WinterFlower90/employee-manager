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

    @ApiOperation(value = "근무 상태 가져오기")
    @GetMapping("/status/member-id/{memberId}")
    public SingleResult<WorkResponse> getStatus(@PathVariable long memberId) {
        return ResponseService.getSingleResult(workService.getCurrentStatus(memberService.getMemberData(memberId)));
    }

    @ApiOperation(value = "근무 상태 수정하기")
    @PutMapping("/status/{workStatus}/member-id/{memberId}")
    public SingleResult<WorkResponse> doStatusChange(
            @PathVariable WorkStatus workStatus,
            @PathVariable long memberId) {
        return ResponseService.getSingleResult(workService.doWorkChange(memberService.getMemberData(memberId), workStatus));
    }

    @ApiOperation(value = "사원별 출근 내역 리스트 가져오기")
    @GetMapping("/member/search/{memberId}")
    public ListResult<WorkDetail> getMemberWorkDetails(@PathVariable long memberId) {
        return ResponseService.getListResult(workService.getMemberWorkDetails(memberId), true);
    }


    //

    @ApiOperation(value = "기간별 출근 내역 리스트 가져오기")
    @GetMapping("/search")
    public ListResult<WorkDetail> getWorkDetails(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateStart")LocalDate dateStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateEnd")LocalDate dateEnd
            ) {
        return ResponseService.getListResult(workService.getWorkDetails(dateStart, dateEnd), true);
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


    /** 관리자용 근무 리스트 가져오기 (필터기능 o) */
    @ApiOperation(value = "관리자용 근무 리스트 가져오기")
    @PostMapping("/works/page/{pageNum}")
    public ListResult<WorkAdminListItem> getWorkListByAdmin(@PathVariable int pageNum, @RequestBody @Valid WorkSearchRequest workSearchRequest) {
        return ResponseService.getListResult(workService.getList(pageNum, workSearchRequest), true);
    }

    /** 특정 년 월의 근무 횟수 조회하기 */
    @ApiOperation(value = "특정 사원의 특정 년 월의 근무 횟수 조회하기")
    @GetMapping("/search/count/{memberId}")
    public CommonResult getCountByMyYearMonth(@PathVariable long memberId, int year, int month) {
        Member member = memberService.getMemberData(memberId);
        return ResponseService.getSingleResult(workService.getCountByMyYearMonth(member, year, month));
    }


//    @ApiOperation(value = "나의 근태 상태 가져오기")
//    @GetMapping("/my/status/{memberId}")
//    public SingleResult<WorkStatusResponse> getMyStatus(@PathVariable long memberId) {
//        return ResponseService.getSingleResult(workService.getMyStatus(memberId));
//    }
//
//    @ApiOperation(value = "출근 처리하기")
//    @PostMapping("/company-in")
//    public CommonResult setStatusCompanyIn(Member member) {
//        workService.setStatusCompanyIn(member);
//        return ResponseService.getSuccessResult();
//    }


//    @ApiOperation(value = "근무 정보 가져오기(test)")
//    @GetMapping("/work-test/{memberId}")
//    public SingleResult<WorkTestDetail> getWorkTest(@PathVariable long memberId) {
//        return ResponseService.getSingleResult(workService.getWorkTest(memberId));
//    }
//
//    @ApiOperation(value = "근무 정보 기간별 리스트 가져오기(test)")
//    @GetMapping("/work-test/period/{memberId}")
//    public ListResult<WorkTestDetail> getWorkTests(
//            @PathVariable long memberId,
//            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateStart")LocalDate dateStart,
//            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateEnd")LocalDate dateEnd
//    ) {
//        return ResponseService.getListResult(workService.getWorkTests(memberId, dateStart, dateEnd), true);
//    }
//
//    @ApiOperation(value = "특정 사원의 특정 년 월의 근무 횟수 조회하기(test)")
//    @GetMapping("/search/count/test/{memberId}")
//    public CommonResult getCountByMyYearMonthTest(@PathVariable long memberId, int year, int month) {
//        return ResponseService.getSingleResult(workService.getCountByMyYearMonthTest(memberId, year, month));
//    }


    //

//    @ApiOperation(value = "근무 정보 등록하기")
//    @PostMapping("/new")
//    public CommonResult setWork(long memberId) {
//        Member member = memberService.getMemberData(memberId); //해당줄 추가. setWork(Member member) -> (long memberId)로 변환.
//        workService.setWork(member);
//        return ResponseService.getSuccessResult();
//    }
//
//    /** 나의 근태상태 수정하기 - 사원용 */
//    @ApiOperation(value = "나의 근태상태 수정하기")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "workId", value = "근무 시퀀스", required = true),
//            @ApiImplicitParam(name = "workStatus", value = "근무 상태", required = true)
//    })
//    @PutMapping("/my/{memberId}/status/{workStatus}")
//    public CommonResult putStatus(@PathVariable long memberId, @PathVariable WorkStatus workStatus) {
//        Member member = memberService.getMemberData(memberId);
//        workService.putStatus(workStatus, member);
//        return ResponseService.getSuccessResult();
//    }
}
