package com.smwuis.sooksook.web.dto.user;

import com.smwuis.sooksook.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    @ApiModelProperty(notes = "이름", example = "이름", required = true)
    private String name;

    @ApiModelProperty(notes = "아이디", example = "아이디", required = true)
    private String loginId;

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임", required = true)
    private String nickname;

    @ApiModelProperty(notes = "비밀번호", example = "비밀번호", required = true)
    private String password;

    @ApiModelProperty(notes = "한 줄 소개글", example = "한 줄 소개글")
    private String introduction;

    @Builder
    public UserSaveRequestDto(String name, String loginId, String email, String nickname, String password, String introduction) {
        this.name = name;
        this.loginId = loginId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.introduction = introduction;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .loginId(loginId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .introduction(introduction)
                .points(0)
                .rating("새싹 등급")
                .build();
    }

}
