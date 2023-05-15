package com.backend.nutt.service;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.dto.response.IntakeYearResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.IntakeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyIntakeService {
    private final IntakeRepository intakeRepository;

    @Transactional
    public void saveDailyIntake(Member member, IntakeFormRequest request) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        Intake intake = Intake.builder()
                .intakeKcal(request.getIntakeKcal())
                .intakeCarbohydrate(request.getIntakeCarbohydrate())
                .intakeProtein(request.getIntakeProtein())
                .intakeFat(request.getIntakeFat())
                .intakeDate(LocalDate.now())            // 업로드시의 날짜
                .intakeTime(LocalTime.now())            // 업로드시의 시간
                .build();


        intakeRepository.save(intake);
        intake.setMember(member);
    }

    public List<IntakeYearResponse> getDailyIntakeYear(Member member, int year) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return intakeRepository.findByIntakeDateYearOrderByIntakeDateAsc(year)
                .stream()
                .filter(m -> m.getMember().getEmail().equals(member.getEmail()))
                .map(m -> IntakeYearResponse.build(m))
                .collect(Collectors.toList());
    }

    public List<IntakeYearResponse> getDailyIntakeMonth(Member member, int month) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return intakeRepository.findByIntakeDateMonthOrderByIntakeDateAsc(month)
                .stream()
                .filter(m -> m.getMember().getEmail().equals(member.getEmail()))
                .map(m -> IntakeYearResponse.build(m))
                .collect(Collectors.toList());
    }

    public List<IntakeYearResponse> getDailyIntakeDay(Member member, int day) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return intakeRepository.findByIntakeDateDayOrderByIntakeDateAsc(day)
                .stream()
                .filter(m -> m.getMember().getEmail().equals(member.getEmail()))
                .map(m -> IntakeYearResponse.build(m))
                .collect(Collectors.toList());
    }
}
