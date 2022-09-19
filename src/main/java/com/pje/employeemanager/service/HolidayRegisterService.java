package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.HolidayRegister;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.ListResult;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import com.pje.employeemanager.model.holiday.HolidayRegisterItem;
import com.pje.employeemanager.model.holiday.HolidayStatusRequest;
import com.pje.employeemanager.repository.HolidayRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayRegisterService {
    private final HolidayRegisterRepository holidayRegisterRepository;

    /** C : 휴가 신청하기 */
    public void setHolidayRegister(Member member, HolidayApplicationRequest request) {
        HolidayRegister holidayRegister = new HolidayRegister.HolidayBuilder(member, request).build();
        holidayRegisterRepository.save(holidayRegister);
    }

    /** 일자별 휴가 신청 리스트 가져오기 */
    public ListResult<HolidayRegisterItem> getHolidayRegister(LocalDate dateStart, LocalDate dateEnd) {
        LocalDate startDate = LocalDate.of(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
        LocalDate endDate = LocalDate.of(dateEnd.getYear(), dateEnd.getMonthValue(), dateEnd.getDayOfMonth());

        List<HolidayRegister> holidayRegisters = holidayRegisterRepository.findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByIdDesc(dateStart, dateEnd);

        List<HolidayRegisterItem> result = new LinkedList<>();
        holidayRegisters.forEach(holidayRegister -> {
            HolidayRegisterItem addItem = new HolidayRegisterItem.HolidayRegisterItemBuilder(holidayRegister).build();
            result.add(addItem);
        });
        return ListConvertService.settingResult(result);
    }

    /** U : 휴가 승인상태 변경하기 */
    public void putHolidayStatus(long id, HolidayStatusRequest holidayStatusRequest) {
        HolidayRegister holidayRegister = holidayRegisterRepository.findById(id).orElseThrow(CMissingDataException::new);
        holidayRegister.putHolidayStatus(holidayStatusRequest);
        holidayRegisterRepository.save(holidayRegister);
    }
}
