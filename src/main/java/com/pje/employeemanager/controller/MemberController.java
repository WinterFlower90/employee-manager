package com.pje.employeemanager.controller;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.SingleResult;
import com.pje.employeemanager.model.member.*;
import com.pje.employeemanager.service.HolidayService;
import com.pje.employeemanager.service.MemberService;
import com.pje.employeemanager.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "사원 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/V1/member")
public class MemberController {
    private final MemberService memberService;
    private final HolidayService holidayService;

    /** 사원 등록 - 관리자용 */
    @ApiOperation(value = "관리자용 사원 등록 및 연차 등록하기")
    @PostMapping("/new")
    public CommonResult setMember(@RequestBody @Valid MemberJoinRequest joinRequest) {
        Member member = memberService.setMember(joinRequest); //회원 등록 후
//        holidayService.setHoliday(member.getId(), member.getDateJoin()); //연차 갯수 등록
        return ResponseService.getSuccessResult();
    }

    /** 사원, 관리자 조회가능
     * 사내 연락망 확인해야 할 수도 있기 때문에 사원도 확인할 수 있다고 생각함 */
    @ApiOperation(value = "사원 리스트 가져오기")
    @GetMapping("get/list/search")
    public ListResult<MemberItem> getMembers(
            @RequestParam(value = "isWorking", required = false) Boolean isWorking,
            @RequestParam(value = "department", required = false) Department department) {
        if (isWorking == null) {
            return ResponseService.getListResult(memberService.getMembers(), true);
        } else {
            if (department == null) {
                return ResponseService.getListResult(memberService.getMembers(isWorking), true);
            } else {
                return ResponseService.getListResult(memberService.getMembers(department), true);
            }
        }
    }

    /** 관리자 리스트 - 관리자만 가능 */
    @ApiOperation(value = "관리자용 관리자 리스트 가져오기")
    @GetMapping("/search/manager")
    public ListResult<MemberItem> getManagerMembers(Boolean isManager) {
        return ResponseService.getListResult(memberService.getManagerMembers(isManager), true);
    }

    @ApiOperation(value = "사원 정보(단수) 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @GetMapping("/get/list/{memberId}")
    public SingleResult<MemberItem> getMember(@PathVariable long memberId) {
        return ResponseService.getSingleResult(memberService.getMember(memberId));
    }

    @ApiOperation(value = "사원 비밀번호 수정하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/my/password/{memberId}")
    public CommonResult putPassword(@PathVariable long memberId, @RequestBody @Valid MemberPasswordRequest passwordRequest) {
        memberService.putPassword(memberId, passwordRequest);
        return ResponseService.getSuccessResult();
    }

    /** 사원 부서 및 직급 수정하기 - 관리자만 가능 */
    @ApiOperation(value = "관리자용 사원 부서 및 직급 수정하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/department/{memberId}")
    public CommonResult putDepartment(@PathVariable long memberId, @RequestBody @Valid MemberDepartmentRequest departmentRequest) {
        memberService.putDepartment(memberId, departmentRequest);
        return ResponseService.getSuccessResult();
    }

    @ApiOperation(value = "사원 개인정보 및 프로필사진 수정하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/my/personal/{memberId}")
    public CommonResult putPersonalInfo(@PathVariable long memberId, @RequestBody @Valid MemberPersonalInformationRequest request) {
        memberService.putPersonalInfo(memberId, request);
        return ResponseService.getSuccessResult();
    }

    /** 사원에게 관리자 권한 부여하기 - 관리자만 가능 */
    @ApiOperation(value = "관리자용 사원에게 관리자권한 부여하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @PutMapping("/manager/{memberId}")
    public CommonResult putManager(@PathVariable long memberId) {
        memberService.putManager(memberId);
        return ResponseService.getSuccessResult();
    }

    /** 사원 퇴사처리 하기 - 관리자만 가능 */
    @ApiOperation(value = "관리자용 사원을 퇴사처리 하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "사원 시퀀스", required = true)
    })
    @DeleteMapping("/retire/{memberId}")
    public CommonResult putRetire(@PathVariable long memberId) {
        memberService.putMemberRetire(memberId);
        return ResponseService.getSuccessResult();
    }

    /** 회원 리스트 - 관리자용
     * post 구현한 이유는 검색필터 부분을 body로 쓰기 위함. (간단하게 구현하기 위해)
     * get 구현하려면 검색필터의 모든 조건들을 requestParam 으로 다 기재해주어야 함 */
    @ApiOperation(value = "관리자용 회원 리스트 가져오기")
    @PostMapping("/members/page/{pageNum}")
    public ListResult<MemberAdminListItem> getMemberListByAdmin(@PathVariable int pageNum, @RequestBody @Valid MemberSearchRequest memberSearchRequest) {
        return ResponseService.getListResult(memberService.getList(pageNum, memberSearchRequest), true);
    }
}
