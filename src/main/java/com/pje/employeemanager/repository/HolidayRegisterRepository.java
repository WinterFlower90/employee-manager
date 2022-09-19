package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HolidayRegisterRepository extends JpaRepository<HolidayRegister, Long> {
    List<HolidayRegister> findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByIdDesc(LocalDate dateStart, LocalDate dateEnd);
}
