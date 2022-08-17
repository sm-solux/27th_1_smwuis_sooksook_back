package com.smwuis.sooksook.domain.study;

import com.smwuis.sooksook.domain.BaseTimeEntity;
import com.smwuis.sooksook.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Getter
@NoArgsConstructor
public class StudyMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudyMember_ID")
    private Long id; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "User_ID")
    private User userId; // 작성자 (fk)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudyBoard_ID")
    private StudyBoard studyBoardId; // 스터디 게시판 (fk)

    @Column(nullable = false)
    private int posts; // 글 작성 수

    @Column(nullable = false)
    private int comments; // 댓글 작성 수

    @Builder
    public StudyMember(User userId, StudyBoard studyBoardId, int posts, int comments) {
        this.userId = userId;
        this.studyBoardId = studyBoardId;
        this.posts = posts;
        this.comments = comments;
    }

    public void updatePost(int posts) {
        this.posts = posts + 1;
    }

    public void updateComments(int comments) {
        this.comments = comments + 1;
    }

    public void setUser(User user) {
        this.userId = user;
    }

    public void setStudyBoardId(StudyBoard studyBoard) {
        this.studyBoardId = studyBoard;

        if(!studyBoardId.getStudyMemberList().contains(this))
            studyBoardId.getStudyMemberList().add(this);
    }
}

