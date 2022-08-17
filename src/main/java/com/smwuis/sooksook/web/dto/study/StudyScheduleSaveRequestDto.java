package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudySchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class StudyScheduleSaveRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1", required = true)
    private Long studyBoardId;

    @ApiModelProperty(notes = "기한", example = "2022-07-29")
    private Date period;

    @ApiModelProperty(notes = "내용", example = "내용", required = true)
    private String content;

    @Builder
    public StudyScheduleSaveRequestDto(String email, Long studyBoardId, Date period, String content) {
        this.email = email;
        this.studyBoardId = studyBoardId;
        this.period = period;
        this.content = content;
    }

    public StudySchedule toEntity() {
        return StudySchedule.builder()
                .period(period)
                .content(content)
                .build();
    }
}
