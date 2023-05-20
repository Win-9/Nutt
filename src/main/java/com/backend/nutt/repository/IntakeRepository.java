package com.backend.nutt.repository;

import com.backend.nutt.domain.Intake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IntakeRepository extends JpaRepository<Intake, Long> {
    @Query("SELECT i FROM Intake i WHERE YEAR(i.intakeDate) = :year ORDER BY i.intakeDate ASC")
    List<Intake> findByIntakeDateYearOrderByIntakeDateAsc(@Param("year") int year);

    @Query("SELECT i FROM Intake i WHERE YEAR(i.intakeDate) = :year AND MONTH(i.intakeDate) = :month ORDER BY i.intakeDate ASC")
    List<Intake> findByIntakeDateYearMonthOrderByIntakeDateAsc(@Param("year") int year, @Param("month") int month);
}
