package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRegisterRepository extends JpaRepository<HolidayRegister, Long> {
}
