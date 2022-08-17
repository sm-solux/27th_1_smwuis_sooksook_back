package com.smwuis.sooksook.domain.study;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {
    List<StudyComment> findAllByStudyPostIdAndUpIndex(StudyPost studyPost, Long upIndex);
    List<StudyComment> findAllByUpIndex(Long upIndex);
    Long countByCreatedDateTimeBetweenAndStudyPostId(LocalDateTime start, LocalDateTime end, StudyPost studyPostId);
}
