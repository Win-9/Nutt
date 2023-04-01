package com.backend.nutt.controller;

import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.AchieveCheckRequest;
import com.backend.nutt.dto.request.AchieveSetRequest;
import com.backend.nutt.dto.response.DailyAchieveResponse;
import com.backend.nutt.exception.FieldNotBindingException;
import com.backend.nutt.exception.UserNotFoundException;
import com.backend.nutt.service.AchieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/api")
@RequiredArgsConstructor
public class AchieveController {
    private final AchieveService achieveService;

    @PostMapping("/login/set/achieve")
    public ResponseEntity achieveSetController(@AuthenticationPrincipal Member member, AchieveSetRequest achieveSetRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FieldNotBindingException("Not_Valid_Info");
        }

        if (member == null) {
            throw new UserNotFoundException("Not_Exist_User");
        }

        DailyAchieveResponse dailyAchieveResponse = achieveService.calculateKcal(achieveSetRequest, member);
        return ResponseEntity.ok(dailyAchieveResponse);
    }

    @PutMapping("/login/set/achieve/check")
    public ResponseEntity achieveSetCheckController(@AuthenticationPrincipal Member member, AchieveCheckRequest achieveCheckRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FieldNotBindingException("Not_Valid_Info");
        }

        if (member == null) {
            throw new UserNotFoundException("Not_Exist_User");
        }

        DailyAchieveResponse dailyAchieveResponse = achieveService.checkAchieve(member, achieveCheckRequest);
        return ResponseEntity.ok(dailyAchieveResponse);
    }
}
