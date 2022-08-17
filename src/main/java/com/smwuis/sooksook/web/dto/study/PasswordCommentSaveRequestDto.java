package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.PasswordComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordCommentSaveRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1", required = true)
    private Long studyBoardId;

    @ApiModelProperty(notes = "내용", example = "내용", required = true)
    private String content;

    @ApiModelProperty(notes = "상위 댓글 id (상위 댓글이 없으면 공백)", example = "null", required = true)
    private Long upIndex;

    @Builder
    public PasswordCommentSaveRequestDto(String email, Long studyBoardId, String content, Long upIndex) {
        this.email = email;
        this.studyBoardId = studyBoardId;
        this.content = content;
        this.upIndex = upIndex;
    }

    public PasswordComment toEntity() {
        return PasswordComment.builder()
                .content(content)
                .upIndex(upIndex)
                .build();
    }
}
