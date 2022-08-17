package com.smwuis.sooksook.domain.study;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.smwuis.sooksook.domain.BaseTimeEntity;
import com.smwuis.sooksook.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class StudyPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudyPost_ID")
    private Long id; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "User_ID")
    private User userId; // 작성자 (fk)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudyBoard_ID")
    private StudyBoard studyBoardId; // 스터디 게시판 (fk)

    @Column(length = 100, nullable = false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    @Column(nullable = false)
    private String category; // 카테고리 (강의 스터디 게시글, 강의 외 스터디 게시글, 질문 게시글, 자료 공유 게시글, 판매/나눔 게시글

    @OneToMany(mappedBy = "studyPostId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyFiles> studyFiles = new ArrayList<>(); // 파일 리스트

    @OneToMany(mappedBy = "studyPostId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyComment> studyComment = new ArrayList<>(); // 댓글 리스트
    
    @Builder
    public StudyPost(User userId, StudyBoard studyBoardId, String title, String content, String category) {
        this.userId = userId;
        this.studyBoardId = studyBoardId;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public StudyPost update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public void setUser(User user) {
        this.userId = user;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStudyBoardId(StudyBoard studyBoard) {
        this.studyBoardId = studyBoard;

        if(!studyBoardId.getStudyPostList().contains(this))
            studyBoardId.getStudyPostList().add(this);
    }

    public void addStudyFiles(StudyFiles studyFiles) {
        this.studyFiles.add(studyFiles);

        if (studyFiles.getStudyPostId() != this)
            studyFiles.setStudyPost(this);
    }

    public void addStudyComment(StudyComment studyComment) {
        this.studyComment.add(studyComment);

        if (studyComment.getStudyPostId() != this)
            studyComment.setStudyPost(this);
    }

}
