package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class StudyMemberResponseDto {

    @ApiModelProperty(notes = "스터디 멤버 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1")
    private Long studyBoardId;

    @ApiModelProperty(notes = "글 작성 수", example = "1")
    private int posts;
    
    @ApiModelProperty(notes = "댓글 작성 수", example = "1")
    private int comments;

    public StudyMemberResponseDto(StudyMember studyMember) {
        this.id = studyMember.getId();
        this.email = studyMember.getUserId().getEmail();
        this.nickname = studyMember.getUserId().getNickname();
        this.studyBoardId = studyMember.getStudyBoardId().getId();
        this.posts = studyMember.getPosts();
        this.comments = studyMember.getComments();
    }
}
