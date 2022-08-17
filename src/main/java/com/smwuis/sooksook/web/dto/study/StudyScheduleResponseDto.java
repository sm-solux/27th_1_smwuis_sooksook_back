package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudySchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class StudyScheduleResponseDto {

    @ApiModelProperty(notes = "스터디 게시글 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "기한", example = "2022-07-29")
    private Date period;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    public StudyScheduleResponseDto(StudySchedule studySchedule) {
        this.id = studySchedule.getId();
        this.email = studySchedule.getUserId().getEmail();
        this.nickname = studySchedule.getUserId().getNickname();
        this.period = studySchedule.getPeriod();
        this.content = studySchedule.getContent();
    }
}
