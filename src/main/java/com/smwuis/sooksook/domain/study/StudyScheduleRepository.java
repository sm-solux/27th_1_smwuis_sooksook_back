package com.smwuis.sooksook.domain.study;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    Optional<StudySchedule> findByIdAndStudyBoardId(Long id, StudyBoard studyBoardId);
    List<StudySchedule> findByStudyBoardId(StudyBoard studyBoardId);
}
