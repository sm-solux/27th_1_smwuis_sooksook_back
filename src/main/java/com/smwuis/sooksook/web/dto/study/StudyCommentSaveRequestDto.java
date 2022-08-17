package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyCommentSaveRequestDto {

    @ApiModelProperty(notes = "작성자 이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "게시글 id", example = "1", required = true)
    private Long studyPostId;

    @ApiModelProperty(notes = "내용", example = "내용", required = true)
    private String content;

    @ApiModelProperty(notes = "상위 댓글 번호", example = "null", required = true)
    private Long upIndex;

    @Builder
    public StudyCommentSaveRequestDto(String email, Long studyPostId, String content, Long upIndex) {
        this.email = email;
        this.studyPostId = studyPostId;
        this.content = content;
        this.upIndex = upIndex;
    }

    public StudyComment toEntity() {
        return StudyComment.builder()
                .content(content)
                .upIndex(upIndex)
                .build();
    }
}
