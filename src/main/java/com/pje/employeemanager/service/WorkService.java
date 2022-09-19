package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.exception.*;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.work.WorkDetail;
import com.pje.employeemanager.model.work.WorkStatusRequest;
import com.pje.employeemanager.model.work.WorkStatusResponse;
import com.pje.employeemanager.model.work.WorkTimeResetRequest;
import com.pje.employeemanager.repository.HolidayHistoryRepository;
import com.pje.employeemanager.repository.MemberRepository;
import com.pje.employeemanager.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final MemberRepository memberRepository;
    private final HolidayHistoryRepository holidayInfoRepository;


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

}
