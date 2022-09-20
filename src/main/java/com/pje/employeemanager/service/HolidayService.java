package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.enums.HolidayType;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import com.pje.employeemanager.model.holiday.HolidayCountRequest;
import com.pje.employeemanager.model.holiday.HolidayRegisterItem;
import com.pje.employeemanager.model.holiday.HolidayStatusRequest;
import com.pje.employeemanager.repository.HolidayCountRepository;
import com.pje.employeemanager.repository.HolidayHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayCountRepository holidayCountRepository;
    private final HolidayHistoryRepository holidayHistoryRepository;

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

    /** U : 휴가 승인상태 변경하기 - 관리자 가능 */
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
}
