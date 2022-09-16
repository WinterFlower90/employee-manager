package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.enums.WorkStatus;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.work.WorkDetail;
import com.pje.employeemanager.model.work.WorkStatusRequest;
import com.pje.employeemanager.model.work.WorkTimeResetRequest;
import com.pje.employeemanager.repository.MemberRepository;
import com.pje.employeemanager.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final WorkRepository workRepository;
    private final MemberRepository memberRepository;


    /** 근무 등록하기 - 매일 자동등록 */
    public void setWork(Member member, WorkStatus workStatus) {
        Work work = new Work.WorkBuilder(member, workStatus).build();
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

}
