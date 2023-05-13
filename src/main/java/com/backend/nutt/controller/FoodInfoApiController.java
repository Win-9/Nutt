package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.domain.Food;
import com.backend.nutt.dto.response.FoodInfoResponse;
import com.backend.nutt.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Tag(name = "음식정보", description = "음식과 관련한 모든 처리를 하는 API들의 모음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FoodInfoApiController {
    private final FoodService foodService;
    private final String BASE_URL = "https://openapi.foodsafetykorea.go.kr/api";
    private final String KEY = "e2c82b2217fa477594e2";
    private final String DATATYPE = "XML";
    private final int START_IDX = 1;
    private final int END_IDX = 200;

    // 계란후라이 정보 없음
    private List<String> foodList = List.of("만두", "깻잎", "잡곡밥", "제육복음",
            "김치찌개", "삼겹살", "된장찌개", "감자탕", "라면", "피자", "양념치킨", "후라이드치킨",
            "배추김치", "깍두기", "불고기", "고등어구이", "짜장면", "짬뽕", "계란찜", "계란후라이");

    private List<String> foodCodeList = List.of("P001536", "D018388", "P090971", "D018288",
            "D000385", "D000385", "D018480", "D018467", "D018163", "D000304", "D000475", "D018547",
            "D018116", "D018111", "D000016", "D018018", "D018180", "D000167", "P036993");

    @GetMapping("/searchFood")
    @Operation(summary = "기타 음식 정보 제공", description = "10가지 음식 외의 다른 음식에 대한 정보를 찾아와서 성분을 제공합니다.")
    public String searchByFoodNameController(@RequestParam String food) throws IOException {
        StringBuilder result = new StringBuilder();

        String fullURL = BASE_URL + "/" + KEY + "/I2790" + "/" + DATATYPE + "/" + START_IDX + "/" + END_IDX + "/DESC_KOR=" + food;

        URL url = new URL(fullURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");

        BufferedReader rd;
        if (httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        }

        String line = null;

        while ((line = rd.readLine()) != null) {
            result.append(line + "\n");
        }
        rd.close();
        httpURLConnection.disconnect();

        JSONObject jsonObject = XML.toJSONObject(result.toString());

        return jsonObject.toString();
    }

    @GetMapping("/saveFood")
    @Operation(summary = "(프론트엔드 사용X)10가지의 음식정보 파싱 저장", description = "객체 탐지후 영양정보를 얻어올 음식에 대한 정보 10가지를 파싱 후 저장합니다.")
    public ResponseEntity foodSave() throws IOException {

        for(int i = 0; i < foodList.size() - 1; i++) {
            StringBuilder result = new StringBuilder();
            String fullURL = BASE_URL + "/" + KEY + "/I2790" + "/" + DATATYPE + "/" + START_IDX + "/" + END_IDX + "/FOOD_CD=" + foodCodeList.get(i);

            URL url = new URL(fullURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            BufferedReader rd;
            if (httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            String line = null;

            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            rd.close();
            httpURLConnection.disconnect();

            JSONObject jsonObject = XML.toJSONObject(result.toString());

            JSONObject rowObj = jsonObject.getJSONObject("I2790").getJSONObject("row");

            Food food = Food.builder()
                    .name(foodList.get(i))
                    .kcal(rowObj.getDouble("NUTR_CONT1"))
                    .carbohydrate(rowObj.getDouble("NUTR_CONT2"))
                    .protein(rowObj.getDouble("NUTR_CONT3"))
                    .fat(rowObj.getDouble("NUTR_CONT4"))
                    .build();

            foodService.saveFood(food);
        }

        // 계란 후라이
        Food food = Food.builder()
                .name(foodList.get(foodList.size() - 1))
                .kcal(120)
                .carbohydrate(0.43)
                .protein(6.24)
                .fat(6.76)
                .build();

        foodService.saveFood(food);

        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "음식 정보 표시", description = "미리 저장된 10가지의 음식을 검색하면 그에대한 정보를 얻을 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(implementation = FoodInfoResponse.class))),
    })
    @GetMapping("/foodInfo/{foodName}")
    public ResponseEntity searchFood(@PathVariable String foodName) {
        FoodInfoResponse response = foodService.getFoodInfoByName(foodName);
        return ResponseEntity.ok().body(BaseResponse.success(response));
    }

}
