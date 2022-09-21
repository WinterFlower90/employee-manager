package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.enums.HolidayType;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.holiday.*;
import com.pje.employeemanager.model.member.MemberSearchRequest;
import com.pje.employeemanager.model.work.WorkAdminListItem;
import com.pje.employeemanager.model.work.WorkSearchRequest;
import com.pje.employeemanager.repository.HolidayCountRepository;
import com.pje.employeemanager.repository.HolidayHistoryRepository;
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

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayCountRepository holidayCountRepository;
    private final HolidayHistoryRepository holidayHistoryRepository;

    @PersistenceContext
    EntityManager entityManager;

    /** 연차 등록 - 관리자용 */
    public void setHoliday(long memberId, LocalDate dateJoin) {
        HolidayCount holidayCount = new HolidayCount.HolidayCountBuilder(memberId, dateJoin).build();
        holidayCountRepository.save(holidayCount);
    }

    /** 연차 증감 - 관리자용 */
    public void putHolidayCount(Member member, HolidayCountRequest holidayCountRequest) {
        HolidayCount holidayCount = holidayCountRepository.findById(member.getId()).orElseThrow(CMissingDataException::new);

        if (holidayCountRequest.isMinus()) holidayCount.plusCountUse(holidayCountRequest.getIncreaseOrDecreaseValue()); //얀차 차감(사용)이라면 사용 연차 갯수를 늘려주고
        else holidayCount.plusCountTotal(holidayCountRequest.getIncreaseOrDecreaseValue()); //연차 증가(추가부여)라면 총 연차 갯수를 늘려준다.
        holidayCountRepository.save(holidayCount);

        /* 연차 변경 내역 등록 */
        HolidayHistory holidayHistory = new HolidayHistory.HolidayHistoryBuilder(member, holidayCountRequest).build();
        holidayHistoryRepository.save(holidayHistory);
    }


    /** C : 휴가 신청하기 - 사원용 */
    public void setHolidayRegister(Member member, HolidayApplicationRequest request) {
        HolidayHistory holidayHistory = new HolidayHistory.HolidayRegisterBuilder(member, request).build();
        holidayHistoryRepository.save(holidayHistory);
    }

    /** R : 일자별 휴가 신청 리스트 가져오기 */
    public ListResult<HolidayRegisterItem> getHolidayRegister(LocalDate dateStart, LocalDate dateEnd) {
        LocalDate startDate = LocalDate.of(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
        LocalDate endDate = LocalDate.of(dateEnd.getYear(), dateEnd.getMonthValue(), dateEnd.getDayOfMonth());

        List<HolidayHistory> holidayHistories = holidayHistoryRepository.findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByIdDesc(dateStart, dateEnd);

        List<HolidayRegisterItem> result = new LinkedList<>();
        holidayHistories.forEach(holidayHistory -> {
            HolidayRegisterItem addItem = new HolidayRegisterItem.HolidayRegisterItemBuilder(holidayHistory).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** U : 휴가 승인상태 변경하기 - 관리자 가능
     * (exception 보완하기) */
    public void putHolidayStatus(long holidayHistoryId, HolidayStatusRequest holidayStatusRequest) {
        HolidayHistory holidayHistory = holidayHistoryRepository.findById(holidayHistoryId).orElseThrow(CMissingDataException::new);

        if (holidayStatusRequest.getHolidayStatus().equals(HolidayStatus.OK)) {
            HolidayCount holidayCount = holidayCountRepository.findTopByMemberIdOrderByDateHolidayCountStartDesc(holidayHistory.getMember().getId()).orElseThrow(CMissingDataException::new);

            float plusValue = holidayHistory.getHolidayType() == HolidayType.ANNUAL ? 1f : 0.5f;

            holidayCount.putCountUse(plusValue);
            holidayCountRepository.save(holidayCount);
            holidayHistory.putIsHolidayApproval(holidayStatusRequest.getHolidayStatus());

        } else if (holidayStatusRequest.getHolidayStatus().equals(HolidayStatus.CANCEL)) holidayHistory.putHolidayRefusal(holidayStatusRequest.getHolidayStatus());

        holidayHistoryRepository.save(holidayHistory);
    }

    /** 관리자용 휴가 리스트 가져오기 (+검색 기능)
     * 아무 조건도 선택하지 않으면 전체 리스트 가져옴 */
    public ListResult<HolidayAdminListItem> getList(int pageNum, HolidaySearchRequest holidaySearchRequest) {
        PageRequest pageRequest = ListConvertService.getPageable(10);

        Page<HolidayHistory> holidayHistories = getData(pageRequest, holidaySearchRequest); //페이징 된 원본 데이터 가져오기

        ListResult<HolidayAdminListItem> result = new ListResult<>();
        result.setTotalItemCount(holidayHistories.getTotalElements()); //총 데이터 갯수 세팅
        result.setTotalPage(holidayHistories.getTotalPages() == 0 ? 1 : holidayHistories.getTotalPages()); //총 페이지 수 세팅
        result.setCurrentPage(holidayHistories.getPageable().getPageNumber() + 1); //현재 페이지 번호 세팅

        List<HolidayAdminListItem> list = new LinkedList<>();
        holidayHistories.forEach(holidayHistory -> list.add(new HolidayAdminListItem.HolidayAdminListItemBuilder(holidayHistory).build())); //원본 재가공
        result.setList(list);

        return result;
    }

    /** 페이징 된 원본 데이터 가져오기 (+검색) */
    private Page<HolidayHistory> getData(Pageable pageable, HolidaySearchRequest searchRequest) {
        //Criteria = 기준. 조건
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); //조건을 넣을 수 있는 빌더 가져오기
        CriteriaQuery<HolidayHistory> criteriaQuery = criteriaBuilder.createQuery(HolidayHistory.class); //기본 쿼리 생성(member select)

        //실제로는 여기에 기재된 엔티티들만 잘 설정하면 된다.
        Root<HolidayHistory> root = criteriaQuery.from(HolidayHistory.class); //대장 엔티티 설정

        List<Predicate> predicates = new LinkedList<>(); //검색 조건을 넣을 리스트를 생성함
        //StringUtils.length를 쓰는 이유 : String에 length를 쓰게 되면 null값인 경우 에러. (nullpoint exception)
        //common-lang3 패키지의 경우 StringUtils.length를 쓰게 되면 null값을 넣어도 에러를 뱉지 않고 숫자 0을 줌.

        //만약 검색필터에서 아이디 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 holidayHistory라고 지정) username 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getUsername()) > 0) predicates.add(criteriaBuilder.like(root.get("username"), "%" + searchRequest.getUsername() + "%"));

        //만약 검색필터에서 이름 검색하기를 원한다면 (글자수가 0글자 초과일 경우) root entity (위에서 holidayHistory라고 지정) name 필드에서 해당 단어를 포함하는 (like) 조건을 추가
        if (StringUtils.length(searchRequest.getName()) > 0) predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRequest.getName() + "%"));

        if (searchRequest.getHolidayType() != null) predicates.add(criteriaBuilder.equal(root.get("holidayType"), searchRequest.getHolidayType()));
        if (searchRequest.getHolidayStatus() != null) predicates.add(criteriaBuilder.equal(root.get("holidayStatus"), searchRequest.getHolidayStatus()));
        if (searchRequest.getDateDesiredStart() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateDesired"), searchRequest.getDateDesiredStart()));
        if (searchRequest.getDateDesiredEnd() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateDesired"), searchRequest.getDateDesiredEnd()));
        if (searchRequest.getDateApplicationStart() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateApplication"), searchRequest.getDateApplicationStart()));
        if (searchRequest.getDateApplicationEnd() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateApplication"), searchRequest.getDateApplicationEnd()));
        //실제로는 여기까지 기재된 검색 조건들만 잘 기재하면 된다

        Predicate[] predArray = new Predicate[predicates.size()]; //총 where 조건의 갯수를 구함
        predicates.toArray(predArray); //리스트를 배열로 변환
        criteriaQuery.where(predArray); //기본 쿼리에 where 문을 생성해서 붙임

        TypedQuery<HolidayHistory> query = entityManager.createQuery(criteriaQuery); //데이터베이스에 쿼리를 날림

        int totalRows = query.getResultList().size(); //총 데이터 갯수를 가져옴

        //사용자에게 제공 될 결과데이터들을 가져옴
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); //데이터를 가져오는 시작지점
        query.setMaxResults(pageable.getPageSize()); //데이터를 가져오는 종료지점

        return new PageImpl<>(query.getResultList(), pageable, totalRows); //페이징을 구현한 데이터를 반환함 (현재 선택된 페이지의 데이터들, 페이징 객체, 총 데이터 수)
    }
}
