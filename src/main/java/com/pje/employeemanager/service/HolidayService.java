package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.*;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.enums.HolidayType;
import com.pje.employeemanager.exception.CAlreadyHolidayStatusDataException;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.exception.CNoHolidayCountRemainException;
import com.pje.employeemanager.exception.COverlapHolidayDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.holiday.*;
import com.pje.employeemanager.model.member.MemberSearchRequest;
import com.pje.employeemanager.model.work.WorkAdminListItem;
import com.pje.employeemanager.model.work.WorkSearchRequest;
import com.pje.employeemanager.repository.HolidayCountRepository;
import com.pje.employeemanager.repository.HolidayHistoryRepository;
import com.pje.employeemanager.repository.TestHolidayCountRepository;
import com.pje.employeemanager.repository.TestHolidayRepository;
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
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayCountRepository holidayCountRepository;
    private final HolidayHistoryRepository holidayHistoryRepository;

    private final TestHolidayRepository testHolidayRepository;
    private final TestHolidayCountRepository testHolidayCountRepository;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * 연차 초기값 등록하기
     * @param member - 사원 정보 가져오기. memberId
     * @param dateCriteria - 임의의 연차 시작일 지정
     * member에 초기값을 세팅하고 저장하기
     */
    public void setDefaultCount(Member member, LocalDate dateCriteria) {
        // 기준일을 가지고 임의의 연차시작일을 만든다.
        LocalDate dateStart = getCriteriaDateStart(member, dateCriteria);

        // 연차시작일 <= 조회 기준일 and 연차종료일 >= 조회기준일 and 해당회원 조건에 해당하는 데이터를 가져온다.
        Optional<TestHolidayCount> holidayCountCheck = testHolidayCountRepository.findByDateStartLessThanEqualAndDateEndGreaterThanEqualAndMember(dateCriteria, dateCriteria, member);

        if (holidayCountCheck.isEmpty()) {
            // 빌더를 통해 초기값 세팅
            TestHolidayCount holidayCount = new TestHolidayCount.TestHolidayCountBuilder(member, dateStart).build();

            // DB 저장
            testHolidayCountRepository.save(holidayCount);
        }
    }

    /** 연차 갯수 정보 조회하기
     *
     * @param member - 사원 정보 가져오기. memberId
     * @param dateCriteria - 조회 기준일 설정.
     * @return 연차 초기화된 데이터 반환
     */
    public MyHolidayCountResponse getMyCount(Member member, LocalDate dateCriteria) {
        // 연차시작일 <= 조회 기준일 and 연차종료일 >= 조회기준일 and 해당회원 조건에 해당하는 데이터를 가져온다.
        Optional<TestHolidayCount> holidayCount = testHolidayCountRepository.findByDateStartLessThanEqualAndDateEndGreaterThanEqualAndMember(dateCriteria, dateCriteria, member);

        // 만약에 데이터가 있으면 (등록된 연차 초기화 데이터가 있으면)
        if (holidayCount.isPresent()) {
            // 데이터에서 기준 시작일 가져오고
            LocalDate dateStart = holidayCount.get().getDateStart();

            // 데이터에서 기준 종료일 가져오고
            LocalDate dateEnd = holidayCount.get().getDateEnd();

            // 위의 기간에 연차 신청/승인 된 수 가져오고
            long countComplete = testHolidayRepository.countByMemberAndDateHolidayRequestGreaterThanEqualAndDateHolidayRequestLessThanEqualAndIsComplete(member, dateStart, dateEnd, true);

            // 빌더로 값을 넣고 리턴. (기본빌더)
            return new MyHolidayCountResponse.MyHolidayCountResponseBuilder(holidayCount.get(), countComplete).build();
        } else { // 등록된 연차 초기화 데이터가 없으면
            // 임의로 연차 시작일을 구하고
            LocalDate dateStart = getCriteriaDateStart(member, dateCriteria);
            // 연차 시작일에서 1년을 더하고 1일을 빼고
            LocalDate dateEnd = dateStart.plusYears(1).minusDays(1);

            // 임의로 구한 기간에 연차 신청/승인된 수를 가져오고
            long countComplete = testHolidayRepository.countByMemberAndDateHolidayRequestGreaterThanEqualAndDateHolidayRequestLessThanEqualAndIsComplete(member, dateStart, dateEnd, true);

            // 빌더로 값을 넣고 리턴. 위의 빌더와 다른 것 주의.
            return new MyHolidayCountResponse.MyHolidayCountResponseEmptyBuilder(dateStart, dateEnd, countComplete).build();
        }
    }

    /** 등록된 초기연차가 없을 때 기준일을 기준으로 연차 시작일을 구하기
     *
     * @param member - 사원 정보 가져오기. memberId
     * @param dateCriteria - 조회 기준일 설정.
     * @return 입사일 기준의 연차시작일을 만들어 계산하고 반환.
     */
    private LocalDate getCriteriaDateStart(Member member, LocalDate dateCriteria) {
        // 기준일의 년도를 가지고 몇년차인지 구한다.
        // +1의 이유는 예를들면 2020년부터 2022년 근무라고 했을때 2020, 2021, 2022 총 3개를 세야하니까.
        int perYear = dateCriteria.getYear() - member.getDateJoin().getYear() + 1;

        // 입사일 기준으로 년차에 해당하는 연차 기준 시작일을 만들어 리턴한다.
        return LocalDate.of(member.getDateJoin().getYear() + (perYear - 1), member.getDateJoin().getMonthValue(), member.getDateJoin().getDayOfMonth());
    }



    //todo : 연차 증감에 관해서 한번 더 검토해야함
    /** 관리자용 연차 등록 */
    public void setHoliday(long memberId, LocalDate dateJoin) {
        HolidayCount holidayCount = new HolidayCount.HolidayCountBuilder(memberId, dateJoin).build();
        holidayCountRepository.save(holidayCount);
    }

    /** 관리자용 연차 증감 */
    public void putHolidayCount(Member member, HolidayCountRequest holidayCountRequest) {
        HolidayCount holidayCount = holidayCountRepository.findById(member.getId()).orElseThrow(CMissingDataException::new);

        if (holidayCountRequest.isMinus()) holidayCount.plusCountUse(holidayCountRequest.getIncreaseOrDecreaseValue()); //얀차 차감(사용)이라면 사용 연차 갯수를 늘려주고
        else holidayCount.plusCountTotal(holidayCountRequest.getIncreaseOrDecreaseValue()); //연차 증가(추가부여)라면 총 연차 갯수를 늘려준다.
        holidayCountRepository.save(holidayCount);

        /* 연차 변경 내역 등록 */
        HolidayHistory holidayHistory = new HolidayHistory.HolidayHistoryBuilder(member, holidayCountRequest).build();
        holidayHistoryRepository.save(holidayHistory);
    }


    /** C : 휴가 신청하기 - 사원용
     *
     * @param member 사원 정보 가져오기. memberId
     * @param request HolidayRequest 항목 값을 입력받는다.
     * 휴가내역에 저장한다.
     */
    public void setHolidayCreate(Member member, HolidayCreateRequest request) {
        HolidayHistory holidayHistory = new HolidayHistory.HolidayCreateBuilder(member, request).build();
        holidayHistoryRepository.save(holidayHistory);
    }

    public ListResult<HolidayRegisterItem> getHolidayRegister() {
        List<HolidayHistory> holidayHistories = holidayHistoryRepository.findAll();

        List<HolidayRegisterItem> result = new LinkedList<>();
        holidayHistories.forEach(holidayHistory -> {
            HolidayRegisterItem addItem = new HolidayRegisterItem.HolidayRegisterItemBuilder(holidayHistory).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

//    /** R : 일자별 휴가 신청 리스트 가져오기 */
//    public ListResult<HolidayRegisterItem> getHolidayRegister(LocalDate dateStart, LocalDate dateEnd) {
//        LocalDate startDate = LocalDate.of(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
//        LocalDate endDate = LocalDate.of(dateEnd.getYear(), dateEnd.getMonthValue(), dateEnd.getDayOfMonth());
//
//        List<HolidayHistory> holidayHistories = holidayHistoryRepository.findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByIdDesc(dateStart, dateEnd);
//
//        List<HolidayRegisterItem> result = new LinkedList<>();
//        holidayHistories.forEach(holidayHistory -> {
//            HolidayRegisterItem addItem = new HolidayRegisterItem.HolidayRegisterItemBuilder(holidayHistory).build();
//            result.add(addItem);
//        });
//        return ListConvertService.settingResult(result);
//    }


    //todo : 관리자용 기능 검토하기
    /** 관리자용 사원별 연차 현황 리스트 가져오기 */
    public ListResult<HolidayCountListItem> getHolidayCounts() {
        List<HolidayCount> counts = holidayCountRepository.findAll();
        List<HolidayCountListItem> result = new LinkedList<>();

        counts.forEach(holidayCount -> {
            HolidayCountListItem addItem = new HolidayCountListItem.HolidayCountListItemBuilder(holidayCount).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /**
     * 사원의 연차를 관리자가 승인상태로 만들기
     * @param memberId : 사원 시퀀스
     * @param holidayStatus : 휴가 처리 상태 - 검토중 / 승인 / 반려
     */
    public void putHolidayApproval(long memberId, HolidayStatus holidayStatus) {
        HolidayHistory holidayHistory = holidayHistoryRepository.findById(memberId).orElseThrow(CMissingDataException::new);
        holidayHistory.putHolidayApproval(holidayStatus);
        holidayHistoryRepository.save(holidayHistory);
    }

    /**
     * 사원의 연차를 관리자가 반려상태로 만들기
     * @param memberId : 사원 시퀀스
     * @param holidayStatus : 휴가 처리 상태 - 검토중 / 승인 / 반려
     */
    public void putHolidayRefusal(long memberId, HolidayStatus holidayStatus) {
        HolidayHistory holidayHistory = holidayHistoryRepository.findById(memberId).orElseThrow(CMissingDataException::new);
        holidayHistory.putHolidayRefusal(holidayStatus);
        holidayHistoryRepository.save(holidayHistory);
    }

    /** U : 휴가 승인상태 변경하기 - 관리자 가능
     *
     * @param holidayHistoryId 휴가내역 시퀀스
     * @param holidayStatusRequest 휴가 승인 상태 / 휴가 차감 값을 받는다.
     *  사원의 휴가내역을 저장한다.
     */
    //todo: exception 보완하기
    public void putHolidayStatus(long holidayHistoryId, HolidayStatusRequest holidayStatusRequest) {
        HolidayHistory holidayHistory = holidayHistoryRepository.findById(holidayHistoryId).orElseThrow(CMissingDataException::new);

        if (holidayHistoryRepository.countByDateDesiredAndMember_Id(holidayHistory.getDateDesired(), holidayHistory.getMember().getId()) > 1 ) throw new COverlapHolidayDataException();
        //휴가 신청 날짜와 memberId를 조회해서 데이터가 1개 이상이면 (=중복된 데이터가 2개이상이면) - 이미 동일한 날짜에 신청한 휴가가 있습니다. throw
        if (holidayHistory.getHolidayStatus() != HolidayStatus.NO_STATUS) throw new CAlreadyHolidayStatusDataException(); //휴가상태가 검토중이 아니라면 : 이미 휴가처리가 완료되었습니다. throw

        if (holidayStatusRequest.getHolidayStatus().equals(HolidayStatus.OK)) {
            HolidayCount holidayCount = holidayCountRepository.findTopByMemberIdOrderByDateHolidayCountStartDesc(holidayHistory.getMember().getId()).orElseThrow(CMissingDataException::new);

            float plusValue = holidayHistory.getHolidayType() == HolidayType.ANNUAL ? 1f : 0.5f;

            if (plusValue > holidayCount.getHolidayRemain()) throw new CNoHolidayCountRemainException(); //잔여 연차 갯수가 사용할 연차 갯수보다 작으면 throw

            holidayCount.putCountUse(plusValue);
            holidayCountRepository.save(holidayCount);
            holidayHistory.putIsHolidayApproval(holidayStatusRequest.getHolidayStatus());

        } else if (holidayStatusRequest.getHolidayStatus().equals(HolidayStatus.CANCEL)) holidayHistory.putHolidayRefusal(holidayStatusRequest.getHolidayStatus());

        holidayHistoryRepository.save(holidayHistory);
    }

    /** 관리자용 휴가 리스트 가져오기 (+검색 기능)
     * 아무 조건도 선택하지 않으면 전체 리스트 가져옴
     *
     * @param pageNum
     * @param holidaySearchRequest
     * 검색 필터 : 아이디 / 이름 / 휴가 타입 / 승인 여부 / 휴가 희망일자(검색시작일) - 휴가 희망일자(검색종료일) / 휴가 신청일자(검색시작일) - 휴가 신청일자(검색종료일)
     * @return 리스트 반환
     */
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

    /** 페이징 된 원본 데이터 가져오기 (+검색)
     *
     * @param pageable
     * @param searchRequest
     * 검색 필터 : 아이디 / 이름 / 휴가 타입 / 승인 여부 / 휴가 희망일자(검색시작일) - 휴가 희망일자(검색종료일) / 휴가 신청일자(검색시작일) - 휴가 신청일자(검색종료일)
     * @return 페이징 된 원본 데이터를 조건이 있으면 조건에 맞춰 반환. 조건이 없으면 전체 리스트 반환.
     */
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

    /** 관리자용 휴가 신청 내역 가져오기
     *
     * @param pageNum pageNum값을 받는다.
     * @param searchRequest 검색 필터 : 사원 시퀀스 / 검색 시작일 / 검색 종료일
     * @return 가공된 List-Result 반환
     */
    public ListResult<HolidayAdminListItem> getListByAdmin(int pageNum, HolidayListSearchRequest searchRequest) {
        Page<HolidayHistory> originList;
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        LocalDate startDate = LocalDate.of(searchRequest.getDateStart().getYear(), searchRequest.getDateStart().getMonthValue(), searchRequest.getDateStart().getDayOfMonth());
        LocalDate endDate = LocalDate.of(searchRequest.getDateEnd().getYear(), searchRequest.getDateEnd().getMonthValue(), searchRequest.getDateEnd().getDayOfMonth());

        if (searchRequest.getMemberId() == null) {
            originList = holidayHistoryRepository.findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByDateDesiredDesc(startDate, endDate, pageRequest);
        } else {
            originList = holidayHistoryRepository.findAllByMember_IdAndDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByDateDesiredDesc(searchRequest.getMemberId(), startDate, endDate, pageRequest);
        }

        List<HolidayAdminListItem> result = new LinkedList<>();
        originList.getContent().forEach(holidayHistory -> {
            HolidayAdminListItem addItem = new HolidayAdminListItem.HolidayAdminListItemBuilder(holidayHistory).build();
            result.add(addItem);
        });

        return ListConvertService.settingResult(result, originList.getTotalElements(), originList.getTotalPages(), originList.getPageable().getPageNumber());
    }
}
