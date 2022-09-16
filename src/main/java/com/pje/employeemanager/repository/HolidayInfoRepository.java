package com.pje.employeemanager.repository;

import com.pje.employeemanager.entity.HolidayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayInfoRepository extends JpaRepository<HolidayInfo, Long> {
}
