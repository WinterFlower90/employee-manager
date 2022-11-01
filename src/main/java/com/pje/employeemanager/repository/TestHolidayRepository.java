package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.TestHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TestHolidayRepository extends JpaRepository<TestHoliday, Long> {
    long countByMemberAndDateHolidayRequestGreaterThanEqualAndDateHolidayRequestLessThanEqualAndIsComplete(Member member, LocalDate dateStart, LocalDate dateEnd, boolean isComplete);

}
