package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyBoard;
import com.smwuis.sooksook.domain.study.StudyPost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SearchResponseDto {

    @ApiModelProperty(notes = "생성 날짜 및 시간", example = "2022-07-29 00:00")
    private LocalDateTime createdDateTime;

    @ApiModelProperty(notes = "스터디 게시판 기본키 (null이면 게시글)", example = "1")
    private Long studyBoardId;

    @ApiModelProperty(notes = "게시글 기본키 (null이면 스터디 게시판)", example = "1")
    private Long studyPostId;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "제목", example = "제목")
    private String title;

    @ApiModelProperty(notes = "강의 스터디 모집 게시판 / 강의 외 스터디 모집 게시판 / 질문 게시글 / 판매,나눔 게시글 / 자료 공유 게시글", example = "카테고리")
    private String category;

    public SearchResponseDto(StudyBoard studyBoard) {
        this.createdDateTime = studyBoard.getCreatedDateTime();
        this.studyBoardId = studyBoard.getId();
        this.studyPostId = null;
        this.email = studyBoard.getUserId().getEmail();
        this.nickname = studyBoard.getUserId().getNickname();
        this.title = studyBoard.getTitle();
        this.category = setLecture(studyBoard);
    }

    public SearchResponseDto(StudyPost studyPost) {
        this.createdDateTime = studyPost.getCreatedDateTime();
        this.studyBoardId = null;
        this.studyPostId = studyPost.getId();
        this.email = studyPost.getUserId().getEmail();
        this.nickname = studyPost.getUserId().getNickname();
        this.title = studyPost.getTitle();
        this.category = studyPost.getCategory();
    }

    public String setLecture(StudyBoard studyBoard) {
        if(studyBoard.isLecture()) {
            return "강의 스터디";
        }
        else {
            return  "강의 외 스터디";
        }
    }
}
