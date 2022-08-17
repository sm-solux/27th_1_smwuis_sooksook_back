package com.smwuis.sooksook.web.dto.user;

import com.smwuis.sooksook.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class UserResponseDto {

    @ApiModelProperty(notes = "유저 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "이름", example = "이름")
    private String name;

    @ApiModelProperty(notes = "아이디", example = "아이디")
    private String loginId;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "비밀번호", example = "비밀번호")
    private String password;

    @ApiModelProperty(notes = "한 줄 소개글", example = "한 줄 소개글")
    private String introduction;

    @ApiModelProperty(notes = "포인트", example = "10")
    private int points;

    @ApiModelProperty(notes = "등급", example = "새싹 등급")
    private String rating;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.introduction = user.getIntroduction();
        this.points = user.getPoints();
        this.rating = user.getRating();
    }
}
