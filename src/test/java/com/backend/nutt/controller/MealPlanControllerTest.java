package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.common.TokenFilter;
import com.backend.nutt.config.SecurityConfig;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Role;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.ExceptionResult;
import com.backend.nutt.exception.badrequest.FieldNotBindingException;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.service.DailyIntakeService;
import com.backend.nutt.service.MealPlanService;
import com.backend.nutt.service.S3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.backend.nutt.common.ResponseMessage.DATA_SUCCESSFULLY_PROCESSED;
import static com.backend.nutt.exception.ErrorMessage.NOT_EXIST_MEMBER;
import static com.backend.nutt.exception.ErrorMessage.NOT_VALID_INFO;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MealPlanController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenFilter.class)
        })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MealPlanControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private DailyIntakeService dailyIntakeService;

        @MockBean
        private MealPlanService mealPlanService;

        @MockBean
        private S3Service s3Service;


        @Test
        @WithMockUser
        @DisplayName("[POST] 일일 섭취량 기록 테스트")
        public void saveDailyIntakeTest() throws Exception {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

                String currentDate = now.format(dateFormatter);
                String currentTime = now.format(timeFormatter);

                IntakeFormRequest request = new IntakeFormRequest(
                        "raw String", "raw String", 1.2, 1.2,
                        1.2, 1.2, "raw String",
                        currentDate, currentTime
                );

                Member member = Member.builder()
                        .email("test@naver.com")
                        .role(Role.NORMAL)
                        .password("abcdeftgas12!")
                        .build();

                // given
                given(dailyIntakeService.saveDailyIntake(member, request, "raw String"))
                        .willReturn(new MealPlan());


                // when
                ResultActions result = mockMvc.perform(post("/api/record-intake")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()));

                // then
                result.andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value("DATA_SUCCESSFULLY_PROCESSED"));
        }

        @Test
        @WithMockUser
        @DisplayName("[POST] 일일 섭취량 필드 바인딩에러 테스트")
        public void saveDailyIntakeNotValidMemberTest() throws Exception {
                // given
                given(dailyIntakeService.saveDailyIntake(any(), any(), any()))
                        .willThrow(new FieldNotBindingException(NOT_VALID_INFO));

                // when
                ResultActions result = mockMvc.perform(post("/api/record-intake")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ExceptionResult.fail(400, NOT_VALID_INFO, "any String")))
                        .with(csrf()));

                // then
                result.andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(jsonPath("$.errorMessage").value("NOT_VALID_INFO"));
        }

}