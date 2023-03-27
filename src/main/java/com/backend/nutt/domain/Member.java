package com.backend.nutt.domain;

import com.backend.nutt.common.BaseTimeEntity;
import com.backend.nutt.domain.type.Gender;
import com.backend.nutt.domain.type.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    //
    // TODO: Info
    /**
     * 목표치: 체중감량, 체중 유지, 근육량 증가
     * 활동량: 매우낮은 활동(기초대사량)
     *        가벼운활동
     *        보통
     *        높은활동
     *        매우높은
     */

    // TODO:
    /**
     * 목표치 테이블 필요
     * PAL에따라 목표치가 달라짐 ->
     */
}
