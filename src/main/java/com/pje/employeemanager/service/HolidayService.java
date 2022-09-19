package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
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

    /** 연차 등록 */
    public void setHoliday(long memberId, LocalDate dateJoin) {
        HolidayCount holidayCount = new HolidayCount.HolidayCountBuilder(memberId, dateJoin).build();
        holidayCountRepository.save(holidayCount);
    }

    /** 연차 증감 */
    public void putHolidayCount(Member member, boolean isMinus, float increaseOrDecreaseValue) {
        HolidayCount holidayCount = holidayCountRepository.findById(member.getId()).orElseThrow(CMissingDataException::new);

        if (isMinus) holidayCount.plusCountUse(increaseOrDecreaseValue); //얀차 차감(사용)이라면 사용 연차 갯수를 늘려주고
        else holidayCount.plusCountTotal(increaseOrDecreaseValue); //연차 증가(추가부여)라면 총 연차 갯수를 늘려준다.
        holidayCountRepository.save(holidayCount);

        /* 연차 변경 내역 등록 */
        HolidayHistory holidayHistory = new HolidayHistory.HolidayHistoryBuilder(member, isMinus, increaseOrDecreaseValue).build();
        holidayHistoryRepository.save(holidayHistory);
    }


    /** C : 휴가 신청하기 */
    public void setHolidayRegister(Member member, HolidayApplicationRequest request) {
        HolidayHistory holidayHistory = new HolidayHistory.HolidayRegisterBuilder(member, request).build();
        holidayHistoryRepository.save(holidayHistory);
    }

    /** 일자별 휴가 신청 리스트 가져오기 */
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

    /** U : 휴가 승인상태 변경하기 */
    public void putHolidayStatus(long id, HolidayStatusRequest holidayStatusRequest) {
        HolidayHistory holidayHistory = holidayHistoryRepository.findById(id).orElseThrow(CMissingDataException::new);
        holidayHistory.putHolidayStatus(holidayStatusRequest);
        holidayHistoryRepository.save(holidayHistory);
    }
}
