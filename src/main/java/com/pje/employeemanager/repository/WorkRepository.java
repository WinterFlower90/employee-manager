package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.model.work.WorkDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findAllByDateWorkGreaterThanEqualAndDateWorkLessThanEqualOrderByIdDesc(LocalDate dateStart, LocalDate dateEnd);
    List<Work> findAllByMember_Id(Long memberId);
}
