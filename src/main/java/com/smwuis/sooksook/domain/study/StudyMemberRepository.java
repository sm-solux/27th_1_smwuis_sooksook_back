package com.smwuis.sooksook.domain.study;

import com.smwuis.sooksook.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findAllByStudyBoardId(StudyBoard studyBoardId);
    Optional<StudyMember> deleteByStudyBoardIdAndUserId(StudyBoard studyBoardId, User userId);
    Optional<StudyMember> findByStudyBoardIdAndUserId(StudyBoard studyBoardId, User userId);
    List<StudyMember> findAllByUserId(User userId);
}
