package com.smwuis.sooksook.service.user;

import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.domain.user.UserSchedule;
import com.smwuis.sooksook.domain.user.UserScheduleRepository;
import com.smwuis.sooksook.web.dto.user.UserScheduleRequestDto;
import com.smwuis.sooksook.web.dto.user.UserScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserScheduleService {

    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;

    // 스케줄 등록
    @Transactional
    public UserScheduleResponseDto save(UserScheduleRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        UserSchedule userSchedule = requestDto.toEntity();
        userSchedule.setUser(user);
        user.addUserScheduleList(userScheduleRepository.save(userSchedule));
        userScheduleRepository.save(userSchedule);

        return new UserScheduleResponseDto(userSchedule);
    }

    // 스케줄 수정
    @Transactional
    public UserScheduleResponseDto update(Long id, UserScheduleRequestDto requestDto) {
        UserSchedule userSchedule = userScheduleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 스케줄이 없습니다."));

        if(userSchedule.getUserId().getEmail().equals(requestDto.getEmail())) {
            userSchedule.update(requestDto.getPeriod(),
                    requestDto.getContent());

            return new UserScheduleResponseDto(userSchedule);
        }
        else {
            throw new RuntimeException("유저 스케줄 수정에 실패했습니다.");
        }
    }

    // 스케줄 삭제
    @Transactional
    public Boolean delete(Long id, String email) {
        UserSchedule userSchedule = userScheduleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 스케줄이 없습니다."));
        
        if(userSchedule.getUserId().getEmail().equals(email)) {
            userScheduleRepository.delete(userSchedule);
            return true;
        }
        else {
            throw new RuntimeException("유저 스케줄 삭제에 실패했습니다.");
        }
    }
    
    // 스케줄 완료 체크
    @Transactional
    public UserScheduleResponseDto finish(Long id, String email) {
        UserSchedule userSchedule = userScheduleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 스케줄이 없습니다."));

        if(userSchedule.getUserId().getEmail().equals(email)) {
            userSchedule.updateFinish();
            return new UserScheduleResponseDto(userSchedule);
        }
        else {
            throw new RuntimeException("유저 스케줄 삭제에 실패했습니다.");
        }
    }
    
    // 나의 스케줄 조회
    @Transactional(readOnly = true)
    public List<UserScheduleResponseDto> findMySchedule(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        return userScheduleRepository.findAllByUserId(user)
                .stream()
                .map(UserScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    // 스케줄 상세 조회
    @Transactional(readOnly = true)
    public UserScheduleResponseDto findSchedule(Long id) {
        UserSchedule userSchedule = userScheduleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 스케줄이 없습니다."));
        return new UserScheduleResponseDto(userSchedule);
    }
}
