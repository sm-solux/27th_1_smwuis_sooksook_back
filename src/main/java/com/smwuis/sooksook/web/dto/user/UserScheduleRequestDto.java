package com.smwuis.sooksook.web.dto.user;


import com.smwuis.sooksook.domain.user.UserSchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UserScheduleRequestDto {

    @ApiModelProperty(notes = "이메일", example = "이메일", required = true)
    private String email;

    @ApiModelProperty(notes = "기한", example = "2022-07-28")
    private Date period;

    @ApiModelProperty(notes = "내용", example = "내용", required = true)
    private String content;

    @Builder
    public UserScheduleRequestDto(String email, Date period, String content) {
        this.email = email;
        this.period = period;
        this.content = content;
    }

    public UserSchedule toEntity() {
        return UserSchedule.builder().
                period(period)
                .content(content)
                .build();
    }
}
