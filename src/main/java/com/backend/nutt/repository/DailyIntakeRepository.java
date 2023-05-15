package com.backend.nutt.repository;

import com.backend.nutt.domain.Intake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyIntakeRepository extends JpaRepository<Intake, Long> {
    List<Intake> findByIntakeDateYearOrderByIntakeDateAsc(int year);
    List<Intake> findByIntakeDateMonthOrderByIntakeDateAsc(int month);
    List<Intake> findByIntakeDateDayOrderByIntakeDateAsc(int day);
}
