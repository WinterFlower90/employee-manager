package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.Department;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.exception.CNoMemberDataException;
import com.pje.employeemanager.exception.CNoWorkingMemberDataException;
import com.pje.employeemanager.exception.CNotMatchPasswordException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.member.*;
import com.pje.employeemanager.repository.HolidayHistoryRepository;
import com.pje.employeemanager.repository.MemberRepository;
import com.pje.employeemanager.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final WorkRepository workRepository;
    private final HolidayHistoryRepository holidayInfoRepository;

    /** 데이터를 수동으로 조회하기 위해 필요함.
     * 복잡한 검색 필터를 구현하기 위해 필요 (레포지토리에 쿼리문 입력시 한계가 있음) */
    @PersistenceContext
    EntityManager entityManager;

    /** 로그인.
     *  isManager true = 관리자 로그인, false = 사원 로그인 */
    public MemberLoginResponse doLogin(Boolean isManager, MemberLoginRequest loginRequest) {
        Member member = memberRepository.findByUsernameAndIsManager(loginRequest.getUsername(), isManager).orElseThrow(CMissingDataException::new);

        /* 회원정보에 저장된 비밀번호와 사용자가 입력한 비밀번호가 같지 않을때 */
        if (!member.getPassword().equals(loginRequest.getPassword())) throw new CNotMatchPasswordException();

        /* 회원이 퇴사상태일때 */
        if (!member.getIsWorking()) throw new CNoWorkingMemberDataException();

        return new MemberLoginResponse.MemberLoginResponseBuilder(member).build();
    }


    /** 사원 등록 - 저장 후 저장한 결과값을 바로 반환하는것이 포인트 */
    public Member setMember(MemberJoinRequest joinRequest) {
        Member member = new Member.MemberBuilder(joinRequest).build();
        return memberRepository.save(member);
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

    /** 관리자용 회원 리스트 가져오기 (+검색 기능)
     * 아무 조건도 선택하지 않으면 전체 리스트 가져옴 */
    public ListResult<MemberAdminListItem> getList(int pageNum, MemberSearchRequest memberSearchRequest) {
        PageRequest pageRequest = ListConvertService.getPageable(10);

        Page<Member> members = getData(pageRequest, memberSearchRequest); //페이징 된 원본 데이터 가져오기

        ListResult<MemberAdminListItem> result = new ListResult<>();
        result.setTotalItemCount(members.getTotalElements()); //총 데이터 갯수 세팅
        result.setTotalPage(members.getTotalPages() == 0 ? 1 : members.getTotalPages()); //총 페이지 수 세팅
        result.setCurrentPage(members.getPageable().getPageNumber() + 1); //현재 페이지 번호 세팅

        List<MemberAdminListItem> list = new LinkedList<>();
        members.forEach(member -> list.add(new MemberAdminListItem.MemberAdminListItemBuilder(member).build())); //원본 재가공
        result.setList(list);

        return result;
    }

    /** 페이징 된 원본 데이터 가져오기 (+검색)
     * private 인 것에 주의. 클래스 안쪽에서만 호출되어야 한다.
     * 페이징 - 사용자가 보는 단위를 작게 쪼갠것을 페이징처리 한다고 한다.
     * 페이징처리를 하지 않게 되면 : 한 페이지에 모든 데이터를 전부 뿌려서 로딩속도 저하. 서버 과부하.*/
    private Page<Member> getData(Pageable pageable, MemberSearchRequest searchRequest) {
        //Criteria = 기준. 조건
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); //조건을 넣을 수 있는 빌더 가져오기
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class); //기본 쿼리 생성(member select)

        //실제로는 여기에 기재된 엔티티들만 잘 설정하면 된다.
        Root<Member> root = criteriaQuery.from(Member.class); //대장 엔티티 설정

        List<Predicate> predicates = new LinkedList<>(); //검색 조건을 넣을 리스트를 생성함

        //만약 검색필터에서 아이디 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 member라고 지정) username 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getUsername()) > 0) predicates.add(criteriaBuilder.like(root.get("username"), "%" + searchRequest.getUsername() + "%"));

        //만약 검색필터에서 이름 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 member라고 지정) name 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getName()) > 0) predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRequest.getName() + "%"));

        //만약 검색필터에서 연락처 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 member라고 지정) phone 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getPhone()) > 0) predicates.add(criteriaBuilder.like(root.get("phone"), "%" + searchRequest.getPhone() + "%"));

        if (searchRequest.getGender() != null) predicates.add(criteriaBuilder.equal(root.get("gender"), searchRequest.getGender()));
        if (searchRequest.getDepartment() != null) predicates.add(criteriaBuilder.equal(root.get("department"), searchRequest.getDepartment()));
        if (searchRequest.getPosition() != null) predicates.add(criteriaBuilder.equal(root.get("position"), searchRequest.getPosition()));
        if (searchRequest.getIsWorking() != null) predicates.add(criteriaBuilder.equal(root.get("isWorking"), searchRequest.getIsWorking()));
        if (searchRequest.getIsManager() != null) predicates.add(criteriaBuilder.equal(root.get("isManager"), searchRequest.getIsManager()));
        if (searchRequest.getDateJoinStart() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateJoin"), searchRequest.getDateJoinStart()));
        if (searchRequest.getDateJoinEnd() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateEnd"), searchRequest.getDateJoinEnd()));
        //실제로는 여기까지 기재된 검색 조건들만 잘 기재하면 된다

        Predicate[] predArray = new Predicate[predicates.size()]; //총 where 조건의 갯수를 구함
        predicates.toArray(predArray); //리스트를 배열로 변환
        criteriaQuery.where(predArray); //기본 쿼리에 where 문을 생성해서 붙임

        TypedQuery<Member> query = entityManager.createQuery(criteriaQuery); //데이터베이스에 쿼리를 날림

        int totalRows = query.getResultList().size(); //총 데이터 갯수를 가져옴

        //사용자에게 제공 될 결과데이터들을 가져옴
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); //데이터를 가져오는 시작지점
        query.setMaxResults(pageable.getPageSize()); //데이터를 가져오는 종료지점

        return new PageImpl<>(query.getResultList(), pageable, totalRows); //페이징을 구현한 데이터를 반환함 (현재 선택된 페이지의 데이터들, 페이징 객체, 총 데이터 수)
    }
}