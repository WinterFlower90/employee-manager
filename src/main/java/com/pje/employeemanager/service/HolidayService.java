package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.entity.HolidayHistory;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.repository.HolidayCountRepository;
import com.pje.employeemanager.repository.HolidayHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        HolidayHistory holidayHistory = new HolidayHistory.HolidayInfoBuilder(member, isMinus, increaseOrDecreaseValue).build();
        holidayHistoryRepository.save(holidayHistory);
    }
}
