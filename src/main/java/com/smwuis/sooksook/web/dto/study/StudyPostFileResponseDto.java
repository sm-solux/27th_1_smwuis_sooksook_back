package com.smwuis.sooksook.web.dto.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StudyPostFileResponseDto {

    @ApiModelProperty(notes = "파일 원본 이름", example = "파일 원본 이름")
    private String origFileName;

    @ApiModelProperty(notes = "파일 이름", example = "파일 이름")
    private String fileName;

    @ApiModelProperty(notes = "파일 위치", example = "파일 위치")
    private String filePath;

    @Builder
    public StudyPostFileResponseDto(String origFileName, String fileName, String filePath) {
        this.origFileName = origFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
