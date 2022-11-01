package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayCount;
import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.TestHolidayCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TestHolidayCountRepository extends JpaRepository<TestHolidayCount, Long> {
    Optional<TestHolidayCount> findByDateStartLessThanEqualAndDateEndGreaterThanEqualAndMember(LocalDate dateCriteria1, LocalDate dateCriteria2, Member member);
}
