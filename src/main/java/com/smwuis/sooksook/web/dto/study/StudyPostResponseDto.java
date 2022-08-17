package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyBoard;
import com.smwuis.sooksook.domain.study.StudyPost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class StudyPostResponseDto {

    @ApiModelProperty(notes = "게시글 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "게시판 id", example = "1")
    private Long studyBoardId;

    @ApiModelProperty(notes = "제목", example = "제목")
    private String title;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    @ApiModelProperty(notes = "게시글 파일 id", example = "[1, 2]")
    private List<Long> fileId;

    @ApiModelProperty(notes = "카테고리", example = "강의 스터디 게시글")
    private String category;

    public StudyPostResponseDto(StudyPost studyPost, List<Long> fileId) {
        this.id = studyPost.getId();
        this.email = studyPost.getUserId().getEmail();
        this.nickname = studyPost.getUserId().getNickname();
        this.studyBoardId = setStudyBoardId(studyPost);
        this.title = studyPost.getTitle();
        this.content = studyPost.getContent();
        this.fileId = fileId;
        this.category = studyPost.getCategory();
    }

    public Long setStudyBoardId(StudyPost studyPost) {
        if(studyPost.getStudyBoardId() == null) {
            return null;
        }
        else {
            return studyPost.getStudyBoardId().getId();
        }
    }
}