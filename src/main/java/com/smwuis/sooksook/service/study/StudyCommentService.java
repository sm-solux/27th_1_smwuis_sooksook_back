package com.smwuis.sooksook.service.study;

import com.smwuis.sooksook.domain.study.*;
import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.study.StudyCommentResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyCommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyCommentService {

    private final StudyCommentRepository studyCommentRepository;
    private final StudyPostRepository studyPostRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;

    // 댓글 작성
    @Transactional
    public StudyCommentResponseDto save(StudyCommentSaveRequestDto saveRequestDto) {
        StudyPost studyPost = studyPostRepository.findById(saveRequestDto.getStudyPostId()).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        User user = userRepository.findByEmail(saveRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        StudyComment studyComment = saveRequestDto.toEntity();
        studyComment.setStudyPost(studyPost);
        studyComment.setUser(user);
        studyPost.addStudyComment(studyCommentRepository.save(studyComment));

        // 스터디 게시글 댓글이 아닌 경우
        if(studyPost.getStudyBoardId() == null) {
            user.updatePoints(user.getPoints());
        }

        // 스터디 게시글 댓글일 경우
        if(studyPost.getStudyBoardId() != null) {
            StudyMember studyMember = studyMemberRepository.findByStudyBoardIdAndUserId(studyPost.getStudyBoardId(), user).orElseThrow(()-> new IllegalArgumentException("해당 스터디원이 없습니다."));
            studyMember.updateComments(studyMember.getComments());
        }

        Long id = studyCommentRepository.save(studyComment).getId();

        if (saveRequestDto.getUpIndex() != null) {
            StudyComment studyCommentParent = studyCommentRepository.findById(saveRequestDto.getUpIndex()).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
            studyCommentParent.getChildList().add(id);
        }

        return new StudyCommentResponseDto(studyComment);
    }
    
    // 댓글 수정
    @Transactional
    public StudyCommentResponseDto update(Long id, String email, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));

        if(user.equals(studyComment.getUserId())) {
            studyComment.update(content);
            return new StudyCommentResponseDto(studyComment);
        }
        else {
            throw new RuntimeException("댓글 수정에 실패했습니다.");
        }
    }

    // 댓글 삭제
    @Transactional
    public Boolean delete(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if(user.equals(studyComment.getUserId())) {
            studyComment.remove();

            List<StudyComment> removableCommentList = findRemovableList(id);
            studyCommentRepository.deleteAll(removableCommentList);
            return true;
        }
        else {
            throw new RuntimeException("댓글 삭제에 실패했습니다.");
        }
    }

    // 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<StudyCommentResponseDto> allList(Long studyPostId) {
        StudyPost studyPost = studyPostRepository.findById(studyPostId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));

        return studyCommentRepository.findAllByStudyPostIdAndUpIndex(studyPost, null)
                .stream()
                .map(StudyCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 상세 조회
    @Transactional(readOnly = true)
    public StudyCommentResponseDto view(Long id) {

        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
        return new StudyCommentResponseDto(studyComment);
    }

    // 댓글과 대댓글 시 댓글 지울 목록
    @Transactional(readOnly = true)
    public List<StudyComment> findRemovableList(Long id) {
        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));

        List<StudyComment> result = new ArrayList<>();

        // 대댓글
        if (studyComment.getUpIndex() != null) {
            StudyComment studyCommentParent = studyCommentRepository.findById(studyComment.getUpIndex()).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
            List<StudyComment> childList = studyCommentRepository.findAllByUpIndex(studyCommentParent.getId());

            if(studyCommentParent.isRemoved() && isAllChildRemoved(studyCommentParent.getId())) {
                result.addAll(childList);
                result.add(studyCommentParent);
            }
        }
        // 댓글
        else {
            if(isAllChildRemoved(studyComment.getId())) {
                result.add(studyComment);
            }
        }
        return result;
    }

    // 모든 자식 댓글이 삭제되었는지 판단
    @Transactional(readOnly = true)
    public boolean isAllChildRemoved(Long id) {

        List<StudyComment> childList = studyCommentRepository.findAllByUpIndex(id);

        return childList.stream()
                .map(StudyComment::isRemoved) // 지워졌는지 여부
                .filter(isRemove -> !isRemove) // 지워졌으면 true, 안 지워졌으면 false 이므로
                .findAny() // 지워지지 않은게 하나라도 있다면 false 반환
                .orElse(true); // 모두 지워졌다면 true 반환
    }
}

