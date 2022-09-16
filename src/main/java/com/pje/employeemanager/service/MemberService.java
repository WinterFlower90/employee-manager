package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.exception.CNoMemberDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.member.*;
import com.pje.employeemanager.repository.MemberRepository;
import com.pje.employeemanager.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final WorkRepository workRepository;

    /** 사원 C */
    public void setMember(MemberJoinRequest joinRequest) {
        Member member = new Member.MemberBuilder(joinRequest).build();
        memberRepository.save(member);
    }

    /** 사원 시퀀스 id로 데이터 가져오기 */
    public Member getMemberData(long id) {
        return memberRepository.findById(id).orElseThrow(CMissingDataException::new);
    }

    /** 사원 정보 가져오기 */
    public MemberItem getMember(long id) {
        Member member = memberRepository.findById(id).orElseThrow(CMissingDataException::new);
        MemberItem result = new MemberItem.MemberItemBuilder(member).build();
        return result;
    }

    /** 전체 직원 리스트 가져오기 (퇴사자 포함) */
    public ListResult<MemberItem> getMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberItem> result = new LinkedList<>();

        members.forEach(member -> {
            MemberItem addItem = new MemberItem.MemberItemBuilder(member).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** 재직중인 직원 리스트 이름 오름차순으로 가져오기 */
    public ListResult<MemberItem> getMembers(Boolean isWorking) {
        List<Member> members = memberRepository.findAllByIsWorkingOrderByNameAsc(isWorking);
        List<MemberItem> result = new LinkedList<>();

        members.forEach(member -> {
            MemberItem addItem = new MemberItem.MemberItemBuilder(member).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** 부서별 직원 리스트 이름 오름차순으로 가져오기 */
    public ListResult<MemberItem> getMembers(Department department) {
        List<Member> members = memberRepository.findAllByDepartmentOrderByNameAsc(department);
        List<MemberItem> result = new LinkedList<>();

        members.forEach(member -> {
            MemberItem addItem = new MemberItem.MemberItemBuilder(member).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** 관리자 직원 리스트 가져오기 */
    public ListResult<MemberItem> getManagerMembers(Boolean isManager) {
        List<Member> members = memberRepository.findAllByIsManagerOrderByIdDesc(isManager);
        List<MemberItem> result = new LinkedList<>();

        members.forEach(member -> {
            MemberItem addItem = new MemberItem.MemberItemBuilder(member).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** 사원 비밀번호 변경하기 */
    public void putPassword(long id, MemberPasswordRequest passwordRequest) {
        Member member = memberRepository.findById(id).orElseThrow(CMissingDataException::new);
        member.putPassword(passwordRequest);
        memberRepository.save(member);
    }

    /** 사원 부서 및 직급 변경하기 */
    public void putDepartment(long id, MemberDepartmentRequest departmentRequest) {
        Member member = memberRepository.findById(id).orElseThrow(CMissingDataException::new);
        member.putDepartment(departmentRequest);
        memberRepository.save(member);
    }

    /** 사원 개인정보(이름/연락처/프로필사진) 변경하기 */
    public void putPersonalInfo(long id, MemberPersonalInformationRequest personalInformationRequest) {
        Member member = memberRepository.findById(id).orElseThrow(CMissingDataException::new);
        member.putPersonalInfo(personalInformationRequest);
        memberRepository.save(member);
    }

    /** 사원 -> 관리자로 권한 변경하기 */
    public void putManager(long id) {
        Member member = memberRepository.findById(id).orElseThrow(CMissingDataException::new);
        member.putManager();
        memberRepository.save(member);
    }

    /** 퇴사 후 데이터 삭제된것처럼 보이게하기 - 실제로 데이터가 삭제되지는 않음 */
    public void putMemberRetire(long id) {
        Member member = memberRepository.findById(id).orElseThrow(CMissingDataException::new);

        if (!member.getIsWorking()) throw new CNoMemberDataException();

        member.putRetire();
        memberRepository.save(member);
    }

    /*
    public MemberDetail loginUserInfo(Member member, Boolean isManager, String userId, String password) {
        MemberDetail memberDetail = new MemberDetail.MemberDetailBuilder(member).build();
        memberRepository.findByIsManagerAndUserIdAndPassword(isManager, userId, password);
        return memberDetail;
    }
     */
}
