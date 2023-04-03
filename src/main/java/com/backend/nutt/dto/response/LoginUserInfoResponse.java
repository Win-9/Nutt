package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserInfoResponse {
    private String id;
    private String name;
    private Gender gender;
    private double height;
    private double weight;

    public static LoginUserInfoResponse build(Member member) {
        return new LoginUserInfoResponse(
                member.getEmail(),
                member.getName(),
                member.getGender(),
                member.getHeight(),
                member.getWeight());
    }
}
