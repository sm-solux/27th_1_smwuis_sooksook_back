package com.smwuis.sooksook.domain.study;

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
public class StudyComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudyComment_ID")
    private Long id; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "User_ID")
    private User userId; // 작성자 (fk)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudyPost_ID")
    private StudyPost studyPostId; // 게시글 (fk)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    private Long upIndex; // 상위 댓글 번호

    @ElementCollection(targetClass = Long.class)
    private List<Long> childList = new ArrayList<>(); // 자식 댓글 아이디 리스트

    @Column(nullable = false)
    private boolean isRemoved = false; // 댓글 삭제 여부

    @Builder
    public StudyComment(User userId, StudyPost studyPostId, String content, Long upIndex, List<Long> childList, boolean isRemoved) {
        this.userId = userId;
        this.studyPostId = studyPostId;
        this.content = content;
        this.upIndex = upIndex;
        this.childList = childList;
        this.isRemoved = isRemoved;
    }

    public StudyComment update(String content) {
        this.content = content;
        return this;
    }

    public void remove() {
        this.content = "삭제된 댓글입니다.";
        this.isRemoved = true;
    }

    public void setUser(User user) {
        this.userId = user;
    }

    public void setStudyPost(StudyPost studyPost) {
        this.studyPostId = studyPost;

        if(!studyPost.getStudyComment().contains(this))
            studyPost.getStudyComment().add(this);
    }

}
