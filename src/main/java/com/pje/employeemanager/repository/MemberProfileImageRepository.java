package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.MemberProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
    // 회원 한명당 하나의 프로필 사진만 가져가게끔 하기 위해 이렇게 메서드 설계를 함.
    Optional<MemberProfileImage> findByMemberId(long memberId);

}
