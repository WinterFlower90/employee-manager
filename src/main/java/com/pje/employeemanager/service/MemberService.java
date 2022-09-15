package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.exception.CNoMemberDataException;
import com.pje.employeemanager.model.member.MemberDetail;
import com.pje.employeemanager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDetail loginUserInfo(Member member, Boolean isManager, String userId, String password) {
        MemberDetail memberDetail = new MemberDetail.MemberDetailBuilder(member).build();
        memberRepository.findByIsManagerAndUserIdAndPassword(isManager, userId, password);
        return memberDetail;
    }
}
