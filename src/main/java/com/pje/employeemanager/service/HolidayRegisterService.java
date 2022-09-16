package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.HolidayRegister;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.HolidayStatus;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.holiday.HolidayApplicationRequest;
import com.pje.employeemanager.repository.HolidayRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayRegisterService {
    private final HolidayRegisterRepository holidayRegisterRepository;

    public void setHolidayRegister(Member member, HolidayApplicationRequest request) {
        HolidayRegister holidayRegister = new HolidayRegister.HolidayBuilder(member, request).build();
        holidayRegisterRepository.save(holidayRegister);
    }


}
