package com.smwuis.sooksook.service.study;

import com.smwuis.sooksook.domain.study.PasswordComment;
import com.smwuis.sooksook.domain.study.PasswordCommentRepository;
import com.smwuis.sooksook.domain.study.StudyBoard;
import com.smwuis.sooksook.domain.study.StudyBoardRepository;
import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.study.PasswordCommentResponseDto;
import com.smwuis.sooksook.web.dto.study.PasswordCommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordCommentService {

    private final PasswordCommentRepository passwordCommentRepository;
    private final StudyBoardRepository studyBoardRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public PasswordCommentResponseDto save(PasswordCommentSaveRequestDto saveRequestDto) {
        StudyBoard studyBoard = studyBoardRepository.findById(saveRequestDto.getStudyBoardId()).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        PasswordComment passwordComment = saveRequestDto.toEntity();
        passwordComment.setStudyBoard(studyBoard);
        passwordComment.setUser(userRepository.findByEmail(saveRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다.")));
        studyBoard.addPasswordComments(passwordCommentRepository.save(passwordComment));
        
        if (saveRequestDto.getUpIndex() != null) {
            PasswordComment passwordCommentParent = passwordCommentRepository.findById(saveRequestDto.getUpIndex()).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
            passwordCommentParent.getChildList().add(passwordCommentRepository.save(passwordComment).getId());
        }

        return new PasswordCommentResponseDto(passwordComment);
    }

    // 댓글 수정
    @Transactional
    public PasswordCommentResponseDto update(Long id, String email, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        PasswordComment passwordComment = passwordCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));

        if(user.equals(passwordComment.getUserId())) {
            passwordComment.update(content);
            return new PasswordCommentResponseDto(passwordComment);
        }

        else {
            throw new RuntimeException("댓글 수정에 실패했습니다.");
        }
    }

    // 댓글 삭제
    @Transactional
    public Boolean delete(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        PasswordComment passwordComment = passwordCommentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if(user.equals(passwordComment.getUserId())) {
            passwordComment.remove();
            List<PasswordComment> removableCommentList = findRemovableList(id);
            passwordCommentRepository.deleteAll(removableCommentList);
            return true;
        }
        else {
            throw new RuntimeException("댓글 삭제에 실패했습니다.");
        }
    }

    // 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<PasswordCommentResponseDto> allList(Long studyBoardId) {
        StudyBoard studyBoard = studyBoardRepository.findById(studyBoardId).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        return passwordCommentRepository.findAllByStudyBoardIdAndUpIndex(studyBoard, null)
                .stream()
                .map(PasswordCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 상세 조회
    @Transactional(readOnly = true)
    public PasswordCommentResponseDto view(Long id, String uid) {
        PasswordComment passwordComment = passwordCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));

        // '비밀댓글입니다.' 표시
        if(isSecretPassword(id, uid)) {
            PasswordCommentResponseDto passwordCommentResponseDto = new PasswordCommentResponseDto(passwordComment);
            passwordCommentResponseDto.setContent("비밀댓글입니다.");
            return passwordCommentResponseDto;
        }
        // 댓글 내용 표시
        else {
            return new PasswordCommentResponseDto(passwordComment);
        }
    }

    // 댓글과 대댓글 시 댓글 지울 목록
    @Transactional(readOnly = true)
    public List<PasswordComment> findRemovableList(Long id) {
        PasswordComment passwordComment = passwordCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));

        List<PasswordComment> result = new ArrayList<>();

        // 대댓글
        if (passwordComment.getUpIndex() != null) {
            PasswordComment studyCommentParent = passwordCommentRepository.findById(passwordComment.getUpIndex()).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
            List<PasswordComment> childList = passwordCommentRepository.findAllByUpIndex(studyCommentParent.getId());

            if(studyCommentParent.isRemoved() && isAllChildRemoved(studyCommentParent.getId())) {
                result.addAll(childList);
                result.add(studyCommentParent);
            }
        }
        // 댓글
        else {
            if(isAllChildRemoved(passwordComment.getId())) {
                result.add(passwordComment);
            }
        }
        return result;
    }

    // 모든 자식 댓글이 삭제되었는지 판단
    @Transactional(readOnly = true)
    public boolean isAllChildRemoved(Long id) {

        List<PasswordComment> childList = passwordCommentRepository.findAllByUpIndex(id);

        return childList.stream()
                .map(PasswordComment::isRemoved) // 지워졌는지 여부
                .filter(isRemove -> !isRemove) // 지워졌으면 true, 안 지워졌으면 false 이므로
                .findAny() // 지워지지 않은게 하나라도 있다면 false 반환
                .orElse(true); // 모두 지워졌다면 true 반환
    }

    // 비밀댓글 표시 여부
    @Transactional(readOnly = true)
    private boolean isSecretPassword(Long id, String email) {
        PasswordComment passwordComment = passwordCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        // 대댓글
        if(passwordComment.getUpIndex() != null) {
            PasswordComment passwordCommentParent = passwordCommentRepository.findById(passwordComment.getUpIndex()).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));

            // 댓글 작성자, 댓글의 부모 댓글 작성자, 글 작성자와 같으면 댓글 내용 표시
            if(passwordComment.getUserId().equals(user) || passwordCommentParent.getUserId().equals(user) || passwordComment.getStudyBoardId().getUserId().equals(user)) {
                return false;
            }
            // 그렇지 않을 경우 '비밀댓글입니다.' 표시
            else {
                return true;
            }
        }

        // 댓글인 경우
        else {
            // 댓글 작성자, 글 작성자와 같으면 댓글 내용 표시
            if(passwordComment.getUserId().equals(user) || passwordComment.getStudyBoardId().getUserId().equals(user)) {
                return false;
            }
            // 그렇지 않을 경우 '비밀댓글입니다.' 표시
            else {
                return true;
            }
        }
    }
}