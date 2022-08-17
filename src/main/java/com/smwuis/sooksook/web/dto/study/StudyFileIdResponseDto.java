package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyFiles;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class StudyFileIdResponseDto {

    @ApiModelProperty(notes = "파일 아이디", example = "1")
    private Long fileId;

    public StudyFileIdResponseDto(StudyFiles studyFiles) {
        this.fileId = studyFiles.getId();
    }
}
