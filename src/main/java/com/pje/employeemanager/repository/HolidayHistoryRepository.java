package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayHistoryRepository extends JpaRepository<HolidayHistory, Long> {
}
