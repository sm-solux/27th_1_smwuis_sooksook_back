package com.smwuis.sooksook.domain.study;

import com.smwuis.sooksook.domain.BaseTimeEntity;
import com.smwuis.sooksook.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@ToString
@Getter
@NoArgsConstructor
public class StudySchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudySchedule_ID")
    private Long id; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "User_ID")
    private User userId; // 작성자 (fk)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudyBoard_ID")
    private StudyBoard studyBoardId; // 스터디 게시판 (fk)

    private Date period; // 기한

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    @Builder
    public StudySchedule(User userId, StudyBoard studyBoardId, Date period, String content) {
        this.userId = userId;
        this.studyBoardId = studyBoardId;
        this.period = period;
        this.content = content;
    }

    public StudySchedule update(Date period, String content) {
        this.period = period;
        this.content = content;
        return this;
    }

    public void setUser(User user) {
        this.userId = user;
    }

    public void setStudyBoardId(StudyBoard studyBoard) {
        this.studyBoardId = studyBoard;

        if(!studyBoardId.getStudyScheduleList().contains(this))
            studyBoardId.getStudyScheduleList().add(this);
    }
}
