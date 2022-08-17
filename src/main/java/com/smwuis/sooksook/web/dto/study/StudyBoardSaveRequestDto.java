package com.smwuis.sooksook.web.dto.study;

import com.smwuis.sooksook.domain.study.StudyBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class StudyBoardSaveRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "강의 스터디 게시판 학부", example = "학부")
    private String department;

    @ApiModelProperty(notes = "과목명", example = "과목명", required = true)
    private String subject;

    @ApiModelProperty(notes = "제목", example = "제목", required = true)
    private String title;

    @ApiModelProperty(notes = "내용", example = "내용", required = true)
    private String content;

    @ApiModelProperty(notes = "인원", example = "4")
    private Long number;

    @ApiModelProperty(notes = "온/오프라인", example = "온라인")
    private String onoff;

    @ApiModelProperty(notes = "기간", example = "2022-07-29")
    private Date period;

    @ApiModelProperty(notes = "비밀번호", example = "비밀번호", required = true)
    private String password;

    @ApiModelProperty(notes = "강의 외 게시판 카테고리", example = "카테고리")
    private String category;

    @Builder
    public StudyBoardSaveRequestDto(String email, String department, String subject, String title, String content,
                      Long number, String onoff, Date period, String password, String category) {
        this.email = email;
        this.department = department;
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.number = number;
        this.onoff = onoff;
        this.period = period;
        this.password = password;
        this.category = category;
    }

    public StudyBoard toEntity() {
        return StudyBoard.builder()
                .department(department)
                .subject(subject)
                .title(title)
                .content(content)
                .number(number)
                .onoff(onoff)
                .period(period)
                .password(password)
                .category(category)
                .build();
    }
}
