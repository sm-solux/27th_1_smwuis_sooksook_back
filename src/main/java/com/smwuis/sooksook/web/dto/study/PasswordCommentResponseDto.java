package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.PasswordComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PasswordCommentResponseDto {

    @ApiModelProperty(notes = "비밀댓글 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "작성자 이메일", example = "이메일")
    private String email;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1")
    private Long studyBoardId;

    @ApiModelProperty(notes = "닉네임", example = "닉네임")
    private String nickname;

    @ApiModelProperty(notes = "내용", example = "내용")
    private String content;

    @ApiModelProperty(notes = "상위 댓글 번호", example = "1")
    private Long upIndex;

    @ApiModelProperty(notes = "자식 댓글 번호", example = "[1, 2, 3]")
    private List<Long> childList;

    @ApiModelProperty(notes = "댓글 삭제 여부", example = "false")
    private boolean isRemoved;

    public PasswordCommentResponseDto(PasswordComment passwordComment) {
        this.id = passwordComment.getId();
        this.email = passwordComment.getUserId().getEmail();
        this.studyBoardId = passwordComment.getStudyBoardId().getId();
        this.nickname = passwordComment.getUserId().getNickname();
        this.content = passwordComment.getContent();
        this.upIndex = passwordComment.getUpIndex();
        this.childList = passwordComment.getChildList();
        this.isRemoved = passwordComment.isRemoved();
    }
}