package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class StudyBoardResponseDto {

    @ApiModelProperty(notes = "스터디 게시판 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "강의 스터디 게시판 학부", example = "학부")
    private String department;

    @ApiModelProperty(notes = "과목명", example = "과목명")
    private String subject;

    @ApiModelProperty(notes = "제목", example = "제목")
    private String title;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    @ApiModelProperty(notes = "인원", example = "4")
    private Long number;

    @ApiModelProperty(notes = "온/오프라인", example = "온라인")
    private String onoff;

    @ApiModelProperty(notes = "기간", example = "2022-07-29")
    private Date period;

    @ApiModelProperty(notes = "비밀번호", example = "비밀번호")
    private String password;

    @ApiModelProperty(notes = "강의 스터디인지 강의 외 스터디인지", example = "강의 스터디")
    private String lecture;

    @ApiModelProperty(notes = "강의 외 게시판 카테고리", example = "카테고리")
    private String category;

    @ApiModelProperty(notes = "스터디 종료 여부", example = "false")
    private boolean finished;

    public StudyBoardResponseDto(StudyBoard studyBoard) {
        this.id = studyBoard.getId();
        this.email = studyBoard.getUserId().getEmail();
        this.nickname = studyBoard.getUserId().getNickname();
        this.department = studyBoard.getDepartment();
        this.subject = studyBoard.getSubject();
        this.title = studyBoard.getTitle();
        this.content = studyBoard.getContent();
        this.number = studyBoard.getNumber();
        this.onoff = studyBoard.getOnoff();
        this.period = studyBoard.getPeriod();
        this.password = studyBoard.getPassword();
        this.lecture = setLecture(studyBoard);
        this.category = studyBoard.getCategory();
        this.finished = studyBoard.isFinished();
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
