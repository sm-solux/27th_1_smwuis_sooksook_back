package com.smwuis.sooksook.domain.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyBoardRepository extends JpaRepository<StudyBoard, Long> {
    List<StudyBoard> findByLecture(boolean lecture);
    List<StudyBoard> findByLectureAndDepartment(boolean lecture, String department);
    List<StudyBoard> findByLectureAndCategory(boolean lecture, String category);
    List<StudyBoard> findTop5ByOrderByIdDesc();
    List<StudyBoard> findByTitleContaining(String keyword);
}
