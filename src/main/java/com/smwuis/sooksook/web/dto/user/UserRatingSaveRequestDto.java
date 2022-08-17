package com.smwuis.sooksook.web.dto.user;

import com.smwuis.sooksook.domain.user.UserRating;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRatingSaveRequestDto {

    @ApiModelProperty(notes = "별점 받는 사람 이메일", example = "이메일", required = true)
    private String receiverEmail;

    @ApiModelProperty(notes = "별점 주는 사람 이메일", example = "이메일1", required = true)
    private String giverEmail;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1", required = true)
    private Long studyBoardId;

    @ApiModelProperty(notes = "과목명", example = "과목명", required = true)
    private String subject;

    @ApiModelProperty(notes = "평가 내용", example = "성실해요", required = true)
    private String contents; 

    @ApiModelProperty(notes = "평가 별점", example = "4.5", required = true)
    private float score;

    @Builder
    public UserRatingSaveRequestDto(String receiverEmail, String giverEmail, Long studyBoardId, String subject, String contents, float score) {
        this.receiverEmail = receiverEmail;
        this.giverEmail = giverEmail;
        this.studyBoardId = studyBoardId;
        this.subject = subject;
        this.contents = contents;
        this.score = score;
    }

    public UserRating toEntity() {
        return UserRating.builder()
                .giverEmail(giverEmail)
                .studyBoardId(studyBoardId)
                .subject(subject)
                .contents(contents)
                .score(score)
                .build();
    }
}
