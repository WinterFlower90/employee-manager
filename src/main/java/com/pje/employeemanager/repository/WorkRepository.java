package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.entity.Work;
import com.pje.employeemanager.model.work.WorkDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findAllByDateWorkGreaterThanEqualAndDateWorkLessThanEqualOrderByIdDesc(LocalDate dateStart, LocalDate dateEnd);
    List<Work> findAllByMember_Id(Long memberId);

    Optional<Work> findByDateWorkAndMember_Id(LocalDate dateWork, long memberId);

    long countByMemberAndDateCreateGreaterThanEqualAndDateCreateLessThanEqual(Member member, LocalDateTime dateStart, LocalDateTime dateEnd);
}
