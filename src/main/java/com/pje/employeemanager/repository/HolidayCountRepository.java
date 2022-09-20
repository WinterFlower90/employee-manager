package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HolidayCountRepository extends JpaRepository<HolidayCount, Long> {
    Optional<HolidayCount> findTopByMemberIdOrderByDateHolidayCountStartDesc(long memberId);
}
