package com.smwuis.sooksook.domain.study;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyFilesRepository extends JpaRepository<StudyFiles, Long> {
    List<StudyFiles> findAllByStudyPostId(StudyPost studyPostId);
}
