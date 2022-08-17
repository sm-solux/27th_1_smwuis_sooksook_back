package com.smwuis.sooksook.web.dto.study;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class StudyUpdateVO {

    @ApiParam(name = "이메일", example = "이메일")
    private String email;

    @ApiParam(name = "제목", example = "제목")
    private String title;

    @ApiParam(name = "내용", example = "내용")
    private String content;

    @ApiParam(name = "파일들 (추가할 파일이 없다면 비워두기)")
    private List<MultipartFile> files;

    @ApiParam(name = "삭제할 파일 id (삭제할 파일이 없다면 비워두기)")
    private List<Long> deleteId;
}
