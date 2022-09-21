package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.exception.*;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.member.MemberAdminListItem;
import com.pje.employeemanager.model.member.MemberSearchRequest;
import com.pje.employeemanager.model.work.*;
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
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;


    /** 근무 등록하기 - 매일 자동등록 */
    public void setWork(Member member) {
        Work work = new Work.WorkBuilder(member).build();
        workRepository.save(work);
    }

    /** 출 퇴근 시간 변경하기 - 관리자 가능 */
    public void putWorkTime(long workId, Member member, WorkTimeResetRequest resetRequest) {
        Work work = workRepository.findById(workId).orElseThrow(CMissingDataException::new);
        work.putWorkTime(member, resetRequest);
        workRepository.save(work);
    }


    /** 출근 처리하기 - 상태없음일때만 노출 */
    public void putInWork(long workId, Member member, WorkStatusRequest statusRequest) {
        Work work = workRepository.findById(workId).orElseThrow(CMissingDataException::new);
        work.putInWork(member, statusRequest);
        workRepository.save(work);
    }

    /** 외출 처리하기 - 출근했을때만 노출 */
    public void putPauseWork(long workId, Member member, WorkStatusRequest statusRequest) {
        Work work = workRepository.findById(workId).orElseThrow(CMissingDataException::new);
        work.putPauseWork(member, statusRequest);
        workRepository.save(work);
    }

    /** 복귀 처리하기 - 외출했을때만 노출 */
    public void putReturnWork(long workId, Member member, WorkStatusRequest statusRequest) {
        Work work = workRepository.findById(workId).orElseThrow(CMissingDataException::new);
        work.putReturnWork(member, statusRequest);
        workRepository.save(work);
    }

    /** 퇴근 처리하기 - 출근했을때만 노출 */
    public void putOutWork(long workId, Member member, WorkStatusRequest statusRequest) {
        Work work = workRepository.findById(workId).orElseThrow(CMissingDataException::new);
        work.putOutWork(member, statusRequest);
        workRepository.save(work);
    }


    /** 일자별 근무리스트 가져오기 */
    public ListResult<WorkDetail> getWorkDetails(LocalDate dateStart, LocalDate dateEnd) {
        LocalDate startDate = LocalDate.of(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
        LocalDate endDate = LocalDate.of(dateEnd.getYear(), dateEnd.getMonthValue(), dateEnd.getDayOfMonth());

        List<Work> works = workRepository.findAllByDateWorkGreaterThanEqualAndDateWorkLessThanEqualOrderByIdDesc(startDate, endDate);

        List<WorkDetail> result = new LinkedList<>();
        works.forEach(work -> {
            WorkDetail addItem = new WorkDetail.WorkDetailBuilder(work).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** 근무자별 근무리스트 가져오기 */
    public ListResult<WorkDetail> getMemberWorkDetails(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(CMissingDataException::new);

        List<Work> workDetails = workRepository.findAllByMember_Id(member.getId());
        List<WorkDetail> result = new LinkedList<>();
        workDetails.forEach(work -> {
            WorkDetail addItem = new WorkDetail.WorkDetailBuilder(work).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** 나의 현재 근태상태 가져오기.
     * 기준일과 회원시퀀스가 일치하는 데이터를 가져옴.
     * 만약 없다면 상태없음으로 세팅 */
    public WorkStatusResponse getMyStatus(long memberId) {
        Work work = workRepository.findByDateWorkAndMember_Id(LocalDate.now(), memberId).orElse(new Work.WorkNoneValueBuilder().build());
        return new WorkStatusResponse.WorkStatusResponseBuilder(work).build();
    }

    /** 출근 처리 메서드 */
    public void setStatusCompanyIn(Member member) {
        //상태 조회를 위한 jpa - findByDateWorkAndMember_Id 메서드 재사용
        //"오늘", "이 회원" 의 근태 상태 데이터를 가져다줘 라는 명령이 반복
        Optional<Work> work = workRepository.findByDateWorkAndMember_Id(LocalDate.now(), member.getId());

        //이미 출근을 했다면 (데이터가 있다면)
        if (work.isPresent()) throw new CAlreadyWorkInDataException();

        Work addWork = new Work.WorkBuilder(member).build();
        workRepository.save(addWork);
    }

    /** 조퇴, 외출, 퇴근 메서드 */
    public void putStatus(WorkStatus workStatus, Member member) {
        Optional<Work> work = workRepository.findByDateWorkAndMember_Id(LocalDate.now(), member.getId());

        if (work.isEmpty()) throw new CNoWorkDataException(); //출근 기록이 없습니다.
        if (work.get().getWorkStatus().equals(WorkStatus.LEAVE_WORK)) throw new CAlreadyWorkOutDataException(); //퇴근후에는 상태를 변경할 수 없습니다.
        if (workStatus.equals(WorkStatus.ATTENDANCE)) throw new CAlreadyWorkInDataException(); //근태상태를 다시 출근상태로 변경할 수 없습니다.
        if (workStatus.equals(work.get().getWorkStatus())) throw new CNotChangeSameDataException(); //같은 근태 상태로는 다시 변경할 수 없습니다.

        Work targetWork = work.get();
        targetWork.putStatus(workStatus);
        workRepository.save(targetWork);
    }


    /** 관리자용 근무 리스트 가져오기 (+검색 기능)
     * 아무 조건도 선택하지 않으면 전체 리스트 가져옴 */
    public ListResult<WorkAdminListItem> getList(int pageNum, WorkSearchRequest workSearchRequest) {
        PageRequest pageRequest = ListConvertService.getPageable(10);

        Page<Work> works = getData(pageRequest, workSearchRequest); //페이징 된 원본 데이터 가져오기

        ListResult<WorkAdminListItem> result = new ListResult<>();
        result.setTotalItemCount(works.getTotalElements()); //총 데이터 갯수 세팅
        result.setTotalPage(works.getTotalPages() == 0 ? 1 : works.getTotalPages()); //총 페이지 수 세팅
        result.setCurrentPage(works.getPageable().getPageNumber() + 1); //현재 페이지 번호 세팅

        List<WorkAdminListItem> list = new LinkedList<>();
        works.forEach(work -> list.add(new WorkAdminListItem.WorkAdminListItemBuilder(work).build())); //원본 재가공
        result.setList(list);

        return result;
    }

    /** 페이징 된 원본 데이터 가져오기 (+검색) */
    private Page<Work> getData(Pageable pageable, WorkSearchRequest searchRequest) {
        //Criteria = 기준. 조건
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); //조건을 넣을 수 있는 빌더 가져오기
        CriteriaQuery<Work> criteriaQuery = criteriaBuilder.createQuery(Work.class); //기본 쿼리 생성(work select)

        //실제로는 여기에 기재된 엔티티들만 잘 설정하면 된다.
        Root<Work> root = criteriaQuery.from(Work.class); //대장 엔티티 설정

        List<Predicate> predicates = new LinkedList<>(); //검색 조건을 넣을 리스트를 생성함

        //만약 검색필터에서 아이디 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 work라고 지정) username 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getUsername()) > 0)
            predicates.add(criteriaBuilder.like(root.get("username"), "%" + searchRequest.getUsername() + "%"));

        //만약 검색필터에서 이름 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 work라고 지정) name 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getName()) > 0)
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRequest.getName() + "%"));

        if (searchRequest.getWorkStatus() != null)
            predicates.add(criteriaBuilder.equal(root.get("workStatus"), searchRequest.getWorkStatus()));
        if (searchRequest.getDateWorkStart() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateWork"), searchRequest.getDateWorkStart()));
        if (searchRequest.getDateWorkEnd() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateWork"), searchRequest.getDateWorkEnd()));
        //실제로는 여기까지 기재된 검색 조건들만 잘 기재하면 된다

        Predicate[] predArray = new Predicate[predicates.size()]; //총 where 조건의 갯수를 구함
        predicates.toArray(predArray); //리스트를 배열로 변환
        criteriaQuery.where(predArray); //기본 쿼리에 where 문을 생성해서 붙임

        TypedQuery<Work> query = entityManager.createQuery(criteriaQuery); //데이터베이스에 쿼리를 날림

        int totalRows = query.getResultList().size(); //총 데이터 갯수를 가져옴

        //사용자에게 제공 될 결과데이터들을 가져옴
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); //데이터를 가져오는 시작지점
        query.setMaxResults(pageable.getPageSize()); //데이터를 가져오는 종료지점

        return new PageImpl<>(query.getResultList(), pageable, totalRows); //페이징을 구현한 데이터를 반환함 (현재 선택된 페이지의 데이터들, 페이징 객체, 총 데이터 수)
    }
}
