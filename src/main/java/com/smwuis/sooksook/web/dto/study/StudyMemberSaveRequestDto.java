package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyMemberSaveRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email ;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1", required = true)
    private Long studyBoardId;

    @ApiModelProperty(notes = "비밀번호", example = "비밀번호", required = true)
    private String password;

    @Builder
    public StudyMemberSaveRequestDto(String email, Long studyBoardId, String password) {
        this.email = email;
        this.studyBoardId = studyBoardId;
        this.password = password;
    }

    public StudyMember toEntity() {
        return StudyMember.builder()
                .comments(0)
                .comments(0)
                .build();
    }

}
