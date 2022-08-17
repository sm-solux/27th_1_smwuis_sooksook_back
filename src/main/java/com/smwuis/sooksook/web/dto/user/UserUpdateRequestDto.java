package com.smwuis.sooksook.web.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "이름", example = "이름", required = true)
    private String name;

    @ApiModelProperty(notes = "닉네임", example = "닉네임", required = true)
    private String nickname;

    @ApiModelProperty(notes = "한 줄 소개글", example = "한 줄 소개글", required = true)
    private String introduction;

    @Builder
    public UserUpdateRequestDto(String email, String name, String nickname, String introduction) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
