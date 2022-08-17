package com.smwuis.sooksook.service.study;

import com.smwuis.sooksook.domain.study.StudyFiles;
import com.smwuis.sooksook.domain.study.StudyFilesRepository;
import com.smwuis.sooksook.domain.study.StudyPost;
import com.smwuis.sooksook.domain.study.StudyPostRepository;
import com.smwuis.sooksook.web.dto.study.StudyFileIdResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyPostFileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyFilesService {

    private final StudyFilesRepository studyFilesRepository;
    private final StudyPostRepository studyPostRepository;

    // 파일 아이디에 따른 파일 개별 조회
    @Transactional(readOnly = true)
    public StudyPostFileResponseDto findByFileId(Long id) {
        StudyFiles studyFiles = studyFilesRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 파일이 없습니다."));

        StudyPostFileResponseDto studyPostFileResponseDto = StudyPostFileResponseDto.builder()
                                                            .origFileName(studyFiles.getOrigFileName())
                                                            .fileName(studyFiles.getFileName())
                                                            .filePath(studyFiles.getFilePath())
                                                            .build();

        return studyPostFileResponseDto;
    }

    // 게시글 아이디에 따른 파일 아이디 전체 조회
    @Transactional(readOnly = true)
    public List<StudyFileIdResponseDto> findAllByStudyPost(Long studyPostId) {
        StudyPost studyPost = studyPostRepository.findById(studyPostId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        List<StudyFiles> filesList = studyFilesRepository.findAllByStudyPostId(studyPost);

        return filesList.stream()
                .map(StudyFileIdResponseDto::new)
                .collect(Collectors.toList());
    }

    // 파일 삭제
    @Transactional
    public void delete(Long id) {
        StudyFiles studyFiles = studyFilesRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 파일이 없습니다."));
        studyFilesRepository.delete(studyFiles);
    }
}
