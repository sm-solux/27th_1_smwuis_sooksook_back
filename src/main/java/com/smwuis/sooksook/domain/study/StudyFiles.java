package com.smwuis.sooksook.domain.study;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudyFile_ID")
    private Long id; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudyPost_ID")
    private StudyPost studyPostId; // 게시글 (fk)

    @Column(nullable = false)
    private String origFileName; // 파일 원본 이름

    @Column(nullable = false)
    private String fileName; // 파일 이름

    @Column(nullable = false)
    private String filePath; // 파일 위치
    
    @Builder
    public StudyFiles(String origFileName, String fileName, String filePath) {
        this.origFileName = origFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void setStudyPost(StudyPost studyPostId) {
        this.studyPostId = studyPostId;

        if(!studyPostId.getStudyFiles().contains(this))
            studyPostId.getStudyFiles().add(this);
    }
}
