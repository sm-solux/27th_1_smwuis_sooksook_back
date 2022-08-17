package com.smwuis.sooksook.web.dto.study;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class StudyPostVO {

    @ApiParam(name = "이메일", example = "이메일")
    private String email;

    @ApiParam(name = "스터디 게시판 id (스터디 게시판 게시글이면 id 입력, 아니면 비워두기)", example = "1")
    private Long studyBoardId;

    @ApiParam(name = "제목", example = "제목")
    private String title;

    @ApiParam(name = "내용", example = "내용")
    private String content;

    @ApiParam(name = "파일들 (파일이 없다면 비워두기)")
    private List<MultipartFile> files;
}
