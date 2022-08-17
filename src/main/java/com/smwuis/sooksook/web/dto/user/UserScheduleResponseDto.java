package com.smwuis.sooksook.web.dto.user;

import com.smwuis.sooksook.domain.user.UserSchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class UserScheduleResponseDto {

    @ApiModelProperty(notes = "유저 스케줄 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "작성자 이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "기한", example = "2022-07-28")
    private Date period;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    @ApiModelProperty(notes = "완료 유무", example = "false")
    private boolean finish;

    public UserScheduleResponseDto(UserSchedule userSchedule) {
        id = userSchedule.getId();
        email = userSchedule.getUserId().getEmail();
        period = userSchedule.getPeriod();
        content = userSchedule.getContent();
        finish = userSchedule.isFinish();
    }
}
