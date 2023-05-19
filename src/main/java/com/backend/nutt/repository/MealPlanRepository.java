package com.backend.nutt.repository;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    @Query("SELECT m FROM MealPlan m " +
            "WHERE m.member.id = :id " +
            "AND YEAR(m.intakeDate) = :year " +
            "AND MONTH(m.intakeDate) = :month")
    MealPlan findByMemberIdAndIntakeDate(@Param("id") long id, @Param("year") int year, @Param("month") int month);
    @Query("SELECT m FROM MealPlan m WHERE YEAR(m.intakeDate) = :year ORDER BY m.intakeDate ASC")
    List<MealPlan> findByIntakeDateYearOrderByIntakeDateAsc(@Param("year") int year);

    @Query("SELECT m FROM MealPlan m WHERE YEAR(m.intakeDate) = :year AND MONTH(m.intakeDate) = :month ORDER BY m.intakeDate ASC")
    List<MealPlan> findByIntakeDateYearMonthOrderByIntakeDateAsc(@Param("year") int year, @Param("month") int month);

    @Query("SELECT m FROM MealPlan m WHERE FUNCTION('CONCAT', YEAR(m.intakeDate), '-', MONTH(m.intakeDate)) = :yearMonth")
    List<MealPlan> findByIntakeDateYearMonthAndTitleOrderByIntakeDate(@Param("yearMonth") String yearMonth);
}
