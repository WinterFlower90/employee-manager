package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayCountRepository extends JpaRepository<HolidayCount, Long> {
}
