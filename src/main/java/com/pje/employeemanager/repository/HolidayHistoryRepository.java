package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayHistoryRepository extends JpaRepository<HolidayHistory, Long> {
    List<HolidayHistory> findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByIdDesc(LocalDate dateStart, LocalDate dateEnd);
}
