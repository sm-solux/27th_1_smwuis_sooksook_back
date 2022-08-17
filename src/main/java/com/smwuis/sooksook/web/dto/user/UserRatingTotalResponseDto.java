package com.smwuis.sooksook.web.dto.user;

import com.smwuis.sooksook.domain.user.UserRating;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class UserRatingTotalResponseDto {
    
    @ApiModelProperty(notes = "과목명", example = "과목명")
    private String subject;

    @ApiModelProperty(notes = "스터디 게시판 id", example = "1")
    private Long studyBoardId;

    @ApiModelProperty(notes = "평가 내용 리스트", example = "[성실해요, 지각해요, 꼼꼼해요]")
    private List<String> contents;

    @ApiModelProperty(notes = "평균 평가 별점", example = "4.5")
    private String averageScore;

    public UserRatingTotalResponseDto(UserRating userRating, List<String> contents, String averageScore) {
        this.subject = userRating.getSubject();
        this.studyBoardId = userRating.getStudyBoardId();
        this.contents = contents;
        this.averageScore = averageScore;

    }
}
