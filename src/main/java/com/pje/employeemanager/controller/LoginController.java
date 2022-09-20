package com.pje.employeemanager.controller;

import com.pje.employeemanager.model.member.MemberLoginRequest;
import com.pje.employeemanager.model.member.MemberLoginResponse;
import com.pje.employeemanager.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "로그인 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/V1/login")
public class LoginController {
    private final MemberService memberService;

    @ApiOperation(value = "관리자 로그인")
    @PostMapping("/admin")
    public MemberLoginResponse doAdminLogin(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return memberService.doLogin(true, memberLoginRequest);
    }

    @ApiOperation(value = "사원 로그인")
    @PostMapping("/member")
    public MemberLoginResponse doMemberLogin(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return memberService.doLogin(true, memberLoginRequest);
    }
}
