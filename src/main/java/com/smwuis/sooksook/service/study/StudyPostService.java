package com.smwuis.sooksook.service.study;

import com.smwuis.sooksook.domain.study.*;
import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.study.StudyFileIdResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyPostResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyPostSaveRequestDto;
import com.smwuis.sooksook.web.dto.study.StudyPostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final StudyBoardRepository studyBoardRepository;
    private final StudyFilesRepository studyFilesRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyFilesService studyFilesService;
    private final AwsS3Service awsS3Service;

    // 게시글 저장
    @Transactional
    public StudyPostResponseDto save(StudyPostSaveRequestDto saveRequestDto, List<MultipartFile> files, String category) throws Exception {
        User user = userRepository.findByEmail(saveRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyPost studyPost = saveRequestDto.toEntity();
        studyPost.setUser(user);
        studyPost.setCategory(category);

        if(files != null) {
            List<StudyFiles> filesList = awsS3Service.uploadFile(files);

            if(!filesList.isEmpty()) {
                for(StudyFiles studyFiles: filesList) {
                    studyPost.addStudyFiles(studyFilesRepository.save(studyFiles));
                }
            }
        }

        // 스터디 게시판 게시글이 아닐 경우
        if (saveRequestDto.getStudyBoardId() == null) {
            user.updatePoints(user.getPoints());
        }

        // 스터디 게시판 게시글일 경우
        else {
            StudyBoard studyBoard = studyBoardRepository.findById(saveRequestDto.getStudyBoardId()).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
            studyPost.setStudyBoardId(studyBoard);
            studyBoard.addStudyPost(studyPostRepository.save(studyPost));

            StudyMember studyMember = studyMemberRepository.findByStudyBoardIdAndUserId(studyBoard, user).orElseThrow(()-> new IllegalArgumentException("해당 스터디원이 없습니다."));
            studyMember.updatePost(studyMember.getPosts());
        }

        studyPostRepository.save(studyPost);
        return new StudyPostResponseDto(studyPost, findFileId(studyPost.getId()));
    }
    
    // 게시글 수정
    @Transactional
    public StudyPostResponseDto update(Long id, StudyPostUpdateRequestDto updateRequestDto, List<MultipartFile> files) throws Exception {
        User user = userRepository.findByEmail(updateRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyPost studyPost = studyPostRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));

        if(user.equals(studyPost.getUserId())) {

            // 추가할 파일이 있다면 파일 추가
            if(!CollectionUtils.isEmpty(files)) {
                List<StudyFiles> filesList = awsS3Service.uploadFile(files);

                for(StudyFiles studyFiles: filesList) {
                    studyPost.addStudyFiles(studyFilesRepository.save(studyFiles));
                }
            }

            studyPost.update(updateRequestDto.getTitle(),
                    updateRequestDto.getContent());

            return new StudyPostResponseDto(studyPost, findFileId(studyPost.getId()));
        }
        else {
            throw new RuntimeException("게시글 수정에 실패했습니다.");
        }
    }
    
    // 게시글 삭제
    @Transactional
    public Boolean delete(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyPost studyPost = studyPostRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        List<StudyFiles> studyFiles = studyFilesRepository.findAllByStudyPostId(studyPost);

        if(user.equals(studyPost.getUserId())) {

            for (StudyFiles studyFile:studyFiles) {
                awsS3Service.deleteS3(studyFile.getFileName());
            }

            studyPostRepository.delete(studyPost);
            return true;
        }
        else {
            throw new RuntimeException("게시글 삭제에 실패했습니다.");
        }
    }
    
    // 특정 스터디 게시판 전체 글 아이디 조회
    @Transactional(readOnly = true)
    public List<Long> allList(Long studyBoardId) {
        StudyBoard studyBoard = studyBoardRepository.findById(studyBoardId).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        List<StudyPost> studyPostList = studyPostRepository.findAllByStudyBoardId(studyBoard);
        List<Long> studyPostIdList = new ArrayList<>();

        for (StudyPost studyPost: studyPostList) {
            studyPostIdList.add(studyPost.getId());
        }

        return studyPostIdList;
    }

    // 스터디 게시글 상세 조회
    @Transactional(readOnly = true)
    public StudyPostResponseDto findById(Long id, List<Long> fileId) {
        StudyPost studyPost = studyPostRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        return new StudyPostResponseDto(studyPost, fileId);
    }

    // 카테고리 별 게시글 리스트 조회
    @Transactional(readOnly = true)
    public List<Long> findByCategory(String category) {
        List<StudyPost> studyPostList = studyPostRepository.findByCategory(category);

        List<Long> studyPostIdList = new ArrayList<>();

        for (StudyPost studyPost: studyPostList) {
            studyPostIdList.add(studyPost.getId());
        }

        return studyPostIdList;

    }

    // 게시글 별 파일 아이디 전체 조회
    @Transactional(readOnly = true)
    public List<Long> findFileId(Long id){

        List<StudyFileIdResponseDto> studyFileIdResponseDtoList = studyFilesService.findAllByStudyPost(id);
        List<Long> fileId = new ArrayList<>();

        for (StudyFileIdResponseDto studyFileIdResponseDto : studyFileIdResponseDtoList) {
            fileId.add(studyFileIdResponseDto.getFileId());
        }
        
        return fileId;
    }
}

