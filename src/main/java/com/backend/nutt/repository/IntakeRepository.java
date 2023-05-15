package com.backend.nutt.repository;

import com.backend.nutt.domain.Intake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntakeRepository extends JpaRepository<Intake, Long> {
    List<Intake> findByIntakeDateYearOrderByIntakeDateAsc(int year);
    List<Intake> findByIntakeDateMonthOrderByIntakeDateAsc(int month);
    List<Intake> findByIntakeDateDayOrderByIntakeDateAsc(int day);
}
