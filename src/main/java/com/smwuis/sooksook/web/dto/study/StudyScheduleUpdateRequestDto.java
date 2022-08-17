package com.smwuis.sooksook.web.dto.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class StudyScheduleUpdateRequestDto {

    @ApiModelProperty(notes = "기한", example = "2022-07-29")
    private Date period;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    @Builder
    public StudyScheduleUpdateRequestDto(Date period, String content) {
        this.period = period;
        this.content = content;
    }
}