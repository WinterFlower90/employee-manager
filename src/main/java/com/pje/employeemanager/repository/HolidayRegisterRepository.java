package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HolidayRegisterRepository extends JpaRepository<HolidayRegister, Long> {
}
