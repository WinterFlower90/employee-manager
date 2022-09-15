package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByIsWorkingOrderByNameAsc(Boolean isWorking);
    List<Member> findAllByIsManagerOrderByIdDesc(Boolean isManager);
    List<Member> findAllByDepartmentOrderByNameAsc(Department department);
    List<Member> findByIsManagerAndUserIdAndPassword(Boolean isManager, String userId, String password);
}
