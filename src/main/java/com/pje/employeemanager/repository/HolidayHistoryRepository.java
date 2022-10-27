package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HolidayHistoryRepository extends JpaRepository<HolidayHistory, Long> {
    List<HolidayHistory> findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByIdDesc(LocalDate dateStart, LocalDate dateEnd);
    long countByDateDesiredAndMember_Id(LocalDate dateDesired, long memberId);


    Page<HolidayHistory> findAllByDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByDateDesiredDesc(LocalDate dateStart, LocalDate dateEnd, Pageable pageable);
    Page<HolidayHistory> findAllByMember_IdAndDateDesiredGreaterThanEqualAndDateDesiredLessThanEqualOrderByDateDesiredDesc(Long memberId, LocalDate dateStart, LocalDate dateEnd, Pageable pageable);
}
