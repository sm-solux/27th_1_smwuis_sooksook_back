package com.smwuis.sooksook.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
    Optional<UserSchedule> findByUserId(User userId);
    List<UserSchedule> findAllByUserId(User userId);
}
