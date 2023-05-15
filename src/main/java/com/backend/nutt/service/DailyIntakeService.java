package com.backend.nutt.service;

import com.backend.nutt.domain.DailyIntake;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.DailyIntakeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyIntakeService {
    private final DailyIntakeRepository dailyIntakeRepository;

    @Transactional
    public void saveDailyIntake(Member member, IntakeFormRequest request) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        DailyIntake dailyIntake = DailyIntake.builder()
                .dailyKcal(request.getIntakeKcal())
                .dailyCarbohydrate(request.getIntakeCarbohydrate())
                .dailyProtein(request.getIntakeProtein())
                .dailyFat(request.getIntakeFat())
                .intakeDate(LocalDate.now())            // 업로드시의 날짜
                .intakeTime(LocalTime.now())            // 업로드시의 시간
                .build();


        dailyIntakeRepository.save(dailyIntake);
        dailyIntake.setMember(member);
    }
}
