package com.smwuis.sooksook.web.dto.user;

import com.smwuis.sooksook.domain.user.UserRating;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class UserRatingResponseDto {

    @ApiModelProperty(notes = "유저 평가 기본키", example = "1")
    private Long id;

    @ApiModelProperty(notes = "별점 받는 사람 이메일", example = "이메일")
    private String receiverEmail;

    @ApiModelProperty(notes = "별점 주는 사람 이메일", example = "이메일1")
    private String giverEmail;

    @ApiModelProperty(notes = "과목명", example = "과목명")
    private String subject;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1")
    private Long studyBoardId;

    @ApiModelProperty(notes = "평가 내용", example = "성실해요")
    private String contents;

    @ApiModelProperty(notes = "평가 별점", example = "4.0")
    private float score;

    public UserRatingResponseDto(UserRating userRating) {
        this.id = userRating.getId();
        this.receiverEmail = userRating.getReceiverEmail().getEmail();
        this.giverEmail = userRating.getGiverEmail();
        this.subject = userRating.getSubject();
        this.studyBoardId = userRating.getStudyBoardId();
        this.contents = userRating.getContents();
        this.score = userRating.getScore();

    }
}
