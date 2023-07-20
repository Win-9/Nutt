package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.common.TokenFilter;
import com.backend.nutt.config.SecurityConfig;
import com.backend.nutt.exception.ErrorMessage;
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

import static com.backend.nutt.common.ResponseMessage.DATA_SUCCESSFULLY_PROCESSED;
import static com.backend.nutt.exception.ErrorMessage.NOT_EXIST_MEMBER;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoginController.class,
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

        @MockBean
        private LoginController loginController;


        @Test
        @WithMockUser
        @DisplayName("[POST] 일일 섭취량 기록 테스트")
        public void saveDailyIntakeTest() throws Exception {
                // given


                // when
                mockMvc.perform(post("/api/record-intake")
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .content(objectMapper.writeValueAsString(BaseResponse.success()))
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$..message").value(DATA_SUCCESSFULLY_PROCESSED));
        }

        @Test
        @WithMockUser
        @DisplayName("[POST] 일일 섭취량 미존재 사용자 테스트")
        public void saveDailyIntakeNotValidMemberTest() throws Exception {
                // given
                given(dailyIntakeService.saveDailyIntake(any(), any(), any()))
                        .willThrow(new UserNotFoundException(NOT_EXIST_MEMBER));

                // when
                ResultActions result = mockMvc.perform(post("/api/record-intake")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(objectMapper.writeValueAsString(BaseResponse.success()))
                        .with(csrf()));

                // then
                result.andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(jsonPath("$..errorMessage").value(NOT_EXIST_MEMBER));
        }

}