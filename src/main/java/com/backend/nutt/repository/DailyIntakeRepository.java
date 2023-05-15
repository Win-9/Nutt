package com.backend.nutt.repository;

import com.backend.nutt.domain.DailyIntake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyIntakeRepository extends JpaRepository<DailyIntake, Long> {
}
