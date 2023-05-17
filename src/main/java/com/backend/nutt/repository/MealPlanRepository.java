package com.backend.nutt.repository;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    @Query("SELECT m FROM MealPlan m WHERE YEAR(m.intakeDate) = :year ORDER BY m.intakeDate ASC")
    List<Intake> findByIntakeDateYearOrderByIntakeDateAsc(@Param("year") int year);

    @Query("SELECT m FROM MealPlan m WHERE YEAR(m.intakeDate) = :year AND MONTH(m.intakeDate) = :month ORDER BY m.intakeDate ASC")
    List<Intake> findByIntakeDateYearMonthOrderByIntakeDateAsc(@Param("year") int year, @Param("month") int month);
}
