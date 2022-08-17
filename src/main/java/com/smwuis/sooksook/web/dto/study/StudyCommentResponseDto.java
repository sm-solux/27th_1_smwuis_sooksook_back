package com.smwuis.sooksook.web.dto.study;
import com.smwuis.sooksook.domain.study.StudyComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StudyCommentResponseDto {
    
    @ApiModelProperty(notes = "게시글 댓글 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "게시글 id", example = "1")
    private Long studyPostId;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    @ApiModelProperty(notes = "상위 댓글 번호", example = "1")
    private Long upIndex;

    @ApiModelProperty(notes = "자식 댓글 번호", example = "[1, 2, 3]")
    private List<Long> childList;

    @ApiModelProperty(notes = "댓글 삭제 여부", example = "false")
    private boolean isRemoved;

    public StudyCommentResponseDto(StudyComment studyComment) {
        this.id = studyComment.getId();
        this.email = studyComment.getUserId().getEmail();
        this.nickname = studyComment.getUserId().getNickname();
        this.studyPostId = studyComment.getStudyPostId().getId();
        this.content = studyComment.getContent();
        this.upIndex = studyComment.getUpIndex();
        this.childList = studyComment.getChildList();
        this.isRemoved = studyComment.isRemoved();
    }
}