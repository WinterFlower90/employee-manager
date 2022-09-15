package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByIsWorkingOrderByIdDesc(Boolean isWorking);
    List<Member> findAllByIsManagerOrderByIdDesc(Boolean isManager, String userId, String password);
    List<Member> findByIsManagerAndUserIdAndPassword(Boolean isManager, String userId, String password);

    @Query("select m from Member m where userId = :userId and password = :password")
    Member findMember(String userId, String password);
}
