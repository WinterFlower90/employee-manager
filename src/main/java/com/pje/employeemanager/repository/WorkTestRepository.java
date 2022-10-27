package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.entity.WorkTest;
import com.pje.employeemanager.enums.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkTestRepository extends JpaRepository<WorkTest, Long> {
    Optional<WorkTest> findByDateWorkAndMemberId(LocalDate dateWork, long memberId);

    List<WorkTest> findAllByDateWorkGreaterThanEqualAndDateWorkLessThanEqualOrderByIdDesc(LocalDate dateStart, LocalDate dateEnd);

    long countByMemberIdAndDateCreateGreaterThanEqualAndDateCreateLessThanEqual(long memberId,LocalDateTime dateStart, LocalDateTime dateEnd);
    long countByWorkStatus(WorkStatus workStatus);
}
