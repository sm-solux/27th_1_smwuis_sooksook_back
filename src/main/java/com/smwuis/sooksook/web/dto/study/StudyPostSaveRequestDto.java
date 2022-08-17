package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyPost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyPostSaveRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "스터디 게시판 id (스터디 게시판 게시글이면 id 입력, 아니면 비워두기)", example = "1", required = true)
    private Long studyBoardId;

    @ApiModelProperty(notes = "제목", example = "제목", required = true)
    private String title;

    @ApiModelProperty(notes = "내용", example = "내용", required = true)
    private String content;

    @Builder
    public StudyPostSaveRequestDto(String email, Long studyBoardId, String title, String content) {
        this.email = email;
        this.studyBoardId = studyBoardId;
        this.title = title;
        this.content = content;
    }

    public StudyPost toEntity() {
        return StudyPost.builder()
                .title(title)
                .content(content)
                .build();
    }
}