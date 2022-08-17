package com.smwuis.sooksook.service.study;

import com.smwuis.sooksook.domain.study.StudyBoard;
import com.smwuis.sooksook.domain.study.StudyBoardRepository;
import com.smwuis.sooksook.domain.study.StudyMember;
import com.smwuis.sooksook.domain.study.StudyMemberRepository;
import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.study.StudyBoardResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyMemberResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyMemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyBoardRepository studyBoardRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    // 스터디 참여
    @Transactional
    public Boolean join(Long boardId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyBoard studyBoard = studyBoardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        // 가입하지 않은 멤버라면
        if (!studyMemberRepository.findByStudyBoardIdAndUserId(studyBoard, user).isPresent()) {
            return false;
        }
        // 가입한 멤버라면
        else {
            return true;
        }
    }

    // 스터디 게시판 비밀번호 확인 및 스터디 가입
    @Transactional
    public StudyMemberResponseDto password(StudyMemberSaveRequestDto saveRequestDto) {
        StudyBoard studyBoard = studyBoardRepository.findById(saveRequestDto.getStudyBoardId()).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        if(studyBoard.getPassword().equals(saveRequestDto.getPassword())) {
            StudyMember joinMember = saveRequestDto.toEntity();
            joinMember.setStudyBoardId(studyBoard);
            joinMember.setUser(userRepository.findByEmail(saveRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다.")));
            studyBoard.addStudyMember(studyMemberRepository.save(joinMember));
            studyMemberRepository.save(joinMember);
            return new StudyMemberResponseDto(joinMember);
        }
        else {
            throw new RuntimeException("스터디 게시판 가입에 실패했습니다.");
        }
    }
    
    // 스터디 탈퇴
    @Transactional
    public Boolean drop(String email, Long studyBoardId) {
        StudyBoard studyBoard = studyBoardRepository.findById(studyBoardId).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        
        if(user.getEmail().equals(email)) {
            Optional<StudyMember> studyMember = studyMemberRepository.deleteByStudyBoardIdAndUserId(studyBoard, user);
            return true;
        }
        else {
            throw new RuntimeException("스터디 게시판 탈퇴에 실패했습니다.");
        }
    }

    // 스터디 별 정보 조회 (멤버 이름, 글 작성 수와 댓글 작성 수 등)
    @Transactional(readOnly = true)
    public List<StudyMemberResponseDto> findByAllByStudyBoardId(Long studyBoardId) {
        StudyBoard studyBoard = studyBoardRepository.findById(studyBoardId).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        return studyMemberRepository.findAllByStudyBoardId(studyBoard)
                .stream()
                .map(StudyMemberResponseDto::new)
                .collect(Collectors.toList());
    }

    // 내가 참여 중인 스터디
    @Transactional(readOnly = true)
    public List<StudyBoardResponseDto> myStudy(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        List<StudyMember> studyList = studyMemberRepository.findAllByUserId(user);
        List<Long> studyBoardIdList = new ArrayList<>();
        List<StudyBoard> studyBoardResponseDtoList = new ArrayList<>();

        for(StudyMember studyMember: studyList) {
            studyBoardIdList.add(studyMember.getStudyBoardId().getId());
        }

        for(Long studyBoardId: studyBoardIdList) {
            studyBoardResponseDtoList.add(studyBoardRepository.findById(studyBoardId).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다.")));
        }

        return studyBoardResponseDtoList.stream().map(StudyBoardResponseDto::new).collect(Collectors.toList());
    }
}

