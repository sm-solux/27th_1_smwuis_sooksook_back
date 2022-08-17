package com.smwuis.sooksook.service.user;

import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRating;
import com.smwuis.sooksook.domain.user.UserRatingRepository;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.user.UserRatingResponseDto;
import com.smwuis.sooksook.web.dto.user.UserRatingTotalResponseDto;
import com.smwuis.sooksook.web.dto.user.UserRatingSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRatingService {
    
    private final UserRatingRepository userRatingRepository;
    private final UserRepository userRepository;

    // 유저 평가 생성
    @Transactional
    public UserRatingResponseDto save(UserRatingSaveRequestDto saveRequestDto) {
        User user = userRepository.findByEmail(saveRequestDto.getReceiverEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        UserRating userRating = saveRequestDto.toEntity();
        userRating.setReceiverEmail(user);
        user.addUserRatingList(userRatingRepository.save(userRating));
        userRatingRepository.save(userRating);

        return new UserRatingResponseDto(userRating);
    }
    
    // 유저 평가 수정
    @Transactional
    public UserRatingResponseDto update(Long id, String email, String contents, float score) {
        UserRating userRating = userRatingRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 평가가 없습니다."));

        if(userRating.getGiverEmail().equals(email)) {
            userRating.update(contents, score);
            return new UserRatingResponseDto(userRating);
        }
        else {
            throw new RuntimeException("유저 평가 수정에 실패했습니다.");
        }
    }
    
    // 유저 평가 삭제
    @Transactional
    public Boolean delete(Long id, String email) {
        UserRating userRating = userRatingRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 평가가 없습니다."));
        
        if(userRating.getGiverEmail().equals(email)) {
            userRatingRepository.delete(userRating);
            return true;
        }
        else {
            throw new RuntimeException("유저 평가 삭제에 실패했습니다.");
        }
    }
    
    // 나의 모든 유저 평가 조회
    @Transactional(readOnly = true)
    public List<UserRatingResponseDto> findMyRating(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        return (userRatingRepository.findByReceiverEmail(user))
                .stream()
                .map(UserRatingResponseDto::new)
                .collect(Collectors.toList());
    }

    // 유저 평가 상세 조회
    @Transactional(readOnly = true)
    public UserRatingResponseDto findRating(Long id) {
        UserRating userRating = userRatingRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저 평가가 없습니다."));
        return new UserRatingResponseDto(userRating);

    }

    // 유저 평가 스터디 종합 조회
    @Transactional(readOnly = true)
    public UserRatingTotalResponseDto findRatingWithStudyBoard(String email, Long studyBoardId) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        List<UserRating> userRatingList = userRatingRepository.findByReceiverEmailAndStudyBoardId(user, studyBoardId);
        List<String> contents = new ArrayList<>();

        float score = 0;

        for(UserRating userRating: userRatingList) {
            score = score + userRating.getScore();
            contents.add(userRating.getContents());
        }

        float averageScore = score / userRatingList.size();
        String formattedScore = String.format("%.1f", averageScore);

        return new UserRatingTotalResponseDto(userRatingList.get(0), contents, formattedScore);
    }
}
