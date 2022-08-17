package com.smwuis.sooksook.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    List<UserRating> findByReceiverEmail(User userId);
    List<UserRating> findByReceiverEmailAndStudyBoardId(User userId, Long studyBoardId);
}
