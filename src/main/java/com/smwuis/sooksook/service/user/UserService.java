package com.smwuis.sooksook.service.user;

import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.user.UserResponseDto;
import com.smwuis.sooksook.web.dto.user.UserSaveRequestDto;
import com.smwuis.sooksook.web.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 생성 (회원가입)
    @Transactional
    public UserResponseDto signup(UserSaveRequestDto saveRequestDto) {

        if(userRepository.findByEmailOrLoginId(saveRequestDto.getEmail(), saveRequestDto.getLoginId()) == null) {
            User singUser = User.builder()
                    .name(saveRequestDto.getName())
                    .loginId(saveRequestDto.getLoginId())
                    .email(saveRequestDto.getEmail())
                    .nickname(saveRequestDto.getNickname())
                    .password(passwordEncoder.encode(saveRequestDto.getPassword()))
                    .introduction(saveRequestDto.getIntroduction())
                    .points(0)
                    .rating("새싹등급")
                    .build();
            userRepository.save(singUser);
            return new UserResponseDto(singUser);
        }
        else {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
    }
    
    // 유저 수정
    @Transactional
    public UserResponseDto update(Long id, UserUpdateRequestDto updateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        if(user.getEmail().equals(updateRequestDto.getEmail())) {
            user.update(updateRequestDto.getName(),
                    updateRequestDto.getNickname(),
                    updateRequestDto.getIntroduction());
            return new UserResponseDto(user);
        }
        else {
            throw new RuntimeException("유저 수정에 실패했습니다.");
        }
    }
    
    // 유저 비밀번호 수정
    @Transactional
    public UserResponseDto updatePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        if(passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.updatePassword(passwordEncoder.encode(newPassword));
            return new UserResponseDto(user);
        }
        else {
            throw new RuntimeException("유저 비밀번호 수정에 실패했습니다.");
        }
    }
    
    // 유저 탈퇴
    @Transactional
    public Boolean delete(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        if(user.getEmail().equals(email)) {
            user.setWithdraw();
            return true;
        }
        else {
            throw new RuntimeException("유저 탈퇴에 실패했습니다.");
        }
    }

    // 로그인
    @Transactional
    public UserResponseDto login(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        if (!user.getNickname().equals("탈퇴한 회원")) {
            if(user == null || !passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("로그인에 실패했습니다.");
            }
            return new UserResponseDto(user);
        }
        else {
            throw new RuntimeException("로그인에 실패했습니다. 탈퇴한 회원입니다.");
        }
    }

    // 유저 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto findUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        return new UserResponseDto(user);
    }

}
