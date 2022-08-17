package com.smwuis.sooksook.web.controller.study;

import com.smwuis.sooksook.domain.study.StudyFiles;
import com.smwuis.sooksook.domain.study.StudyFilesRepository;
import com.smwuis.sooksook.domain.study.StudyPost;
import com.smwuis.sooksook.domain.study.StudyPostRepository;
import com.smwuis.sooksook.service.study.AwsS3Service;
import com.smwuis.sooksook.service.study.StudyFilesService;
import com.smwuis.sooksook.service.study.StudyPostService;
import com.smwuis.sooksook.web.dto.study.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "StudyPostController API (게시글 API)")
@RequiredArgsConstructor
@RestController
public class StudyPostController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final StudyPostService studyPostService;
    private final StudyPostRepository studyPostRepository;
    private final StudyFilesRepository studyFilesRepository;
    private final StudyFilesService studyFilesService;
    private final AwsS3Service awsS3Service;

    // 강의 스터디 게시글 작성
    @PostMapping(value = "/studyPost/lecture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "강의 스터디 게시글 작성 (Postman 이용)", notes = "강의 스터디 게시글 작성 API")
    public ResponseEntity<StudyPostResponseDto> lectureSave(StudyPostVO studyPostVO) throws Exception {
        StudyPostSaveRequestDto saveRequestDto = StudyPostSaveRequestDto
                .builder()
                .email(studyPostVO.getEmail())
                .studyBoardId(studyPostVO.getStudyBoardId())
                .title(studyPostVO.getTitle())
                .content(studyPostVO.getContent())
                .build();
        logger.info("lectureSave (강의 스터디 게시글 작성)");
        return ResponseEntity.ok().body(studyPostService.save(saveRequestDto, studyPostVO.getFiles(), "강의 스터디 게시글"));
    }

    // 스터디 외 게시판 게시글 작성
    @PostMapping(value = "/studyPost/notLecture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "강의 외 스터디 게시글 작성 (Postman 이용)", notes = "강의 외 스터디 게시글 작성 API")
    public ResponseEntity<StudyPostResponseDto> notLectureSave(StudyPostVO studyPostVO) throws Exception {
        StudyPostSaveRequestDto saveRequestDto = StudyPostSaveRequestDto
                .builder()
                .email(studyPostVO.getEmail())
                .studyBoardId(studyPostVO.getStudyBoardId())
                .title(studyPostVO.getTitle())
                .content(studyPostVO.getContent())
                .build();
        logger.info("notLectureSave (강의 외 스터디 게시글 작성)");
        return ResponseEntity.ok().body(studyPostService.save(saveRequestDto, studyPostVO.getFiles(), "강의 외 스터디 게시글"));
    }

    // 자료 공유 게시글 작성
    @PostMapping(value = "/studyPost/share", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "자료 공유 게시글 작성 (Postman 이용)", notes = "자료 공유 게시글 작성 API")
    public ResponseEntity<StudyPostResponseDto> shareSave(StudyPostVO studyPostVO) throws Exception {
        StudyPostSaveRequestDto saveRequestDto = StudyPostSaveRequestDto
                .builder()
                .email(studyPostVO.getEmail())
                .studyBoardId(studyPostVO.getStudyBoardId())
                .title(studyPostVO.getTitle())
                .content(studyPostVO.getContent())
                .build();
        logger.info("shareSave (자료 공유 게시글 작성)");
        return ResponseEntity.ok().body(studyPostService.save(saveRequestDto, studyPostVO.getFiles(), "자료 공유 게시글"));
    }

    // 판매/나눔 게시글 작성
    @PostMapping(value = "/studyPost/sell", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "판매/나눔 게시글 작성 (Postman 이용)", notes = "판매/나눔 게시글 작성 API")
    public ResponseEntity<StudyPostResponseDto> sellSave(StudyPostVO studyPostVO) throws Exception {
        StudyPostSaveRequestDto saveRequestDto = StudyPostSaveRequestDto
                .builder()
                .email(studyPostVO.getEmail())
                .studyBoardId(studyPostVO.getStudyBoardId())
                .title(studyPostVO.getTitle())
                .content(studyPostVO.getContent())
                .build();
        logger.info("sellSave (판매/나눔 게시글 작성)");
        return ResponseEntity.ok().body(studyPostService.save(saveRequestDto, studyPostVO.getFiles(), "판매/나눔 게시글"));
    }

    // 질문 게시글 작성
    @PostMapping(value = "/studyPost/question", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "질문 게시글 작성 (Postman 이용)", notes = "질문 게시글 작성 API")
    public ResponseEntity<StudyPostResponseDto> questionSave(StudyPostVO studyPostVO) throws Exception {
        StudyPostSaveRequestDto saveRequestDto = StudyPostSaveRequestDto
                .builder()
                .email(studyPostVO.getEmail())
                .studyBoardId(studyPostVO.getStudyBoardId())
                .title(studyPostVO.getTitle())
                .content(studyPostVO.getContent())
                .build();
        logger.info("questionSave (질문 게시글 작성)");
        return ResponseEntity.ok().body(studyPostService.save(saveRequestDto, studyPostVO.getFiles(), "질문 게시글"));
    }

    // 스터디 게시글 수정
    @PutMapping(value = "/studyPost")
    @ApiOperation(value = "게시글 수정 (Postman 이용)", notes = "게시글 수정 API")
    @ApiImplicitParam(name = "id", value = "게시글 id", example = "1")
    public ResponseEntity<StudyPostResponseDto> update(@RequestParam Long id, StudyUpdateVO studyUpdateVO) throws Exception {

        StudyPostUpdateRequestDto updateRequestDto = StudyPostUpdateRequestDto
                .builder()
                .email(studyUpdateVO.getEmail())
                .title(studyUpdateVO.getTitle())
                .content(studyUpdateVO.getContent())
                .build();

        // 삭제할 파일이 있다면 파일 삭제
        if (!CollectionUtils.isEmpty(studyUpdateVO.getDeleteId())) {
            for(Long deleteFileId: studyUpdateVO.getDeleteId()) {
                awsS3Service.deleteS3(studyFilesService.findByFileId(deleteFileId).getFileName());
                studyFilesService.delete(deleteFileId);
            }
        }

        logger.info("update (게시글 수정)");
        return ResponseEntity.ok().body(studyPostService.update(id, updateRequestDto, studyUpdateVO.getFiles()));
    }

    // 스터디 게시글 삭제
    @DeleteMapping(value = "/studyPost")
    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "게시글 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일"),
    })
    public ResponseEntity<Boolean> delete(@RequestParam Long id, String email) {
        logger.info("delete (게시글 삭제)");
        return ResponseEntity.ok().body(studyPostService.delete(id, email));
    }

    // 특정 스터디 게시판 전체 글 아이디 리스트 조회
    @GetMapping(value = "/studyPosts/studyList")
    @ApiOperation(value = "게시판 id로 특정 스터디 게시판 전체 글 아이디 리스트 조회", notes = "게시판 id로 특정 스터디 게시판 전체 글 아이디 리스트 조회 API")
    @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1")
    public ResponseEntity<List<Long>> allList(@RequestParam Long studyBoardId) {
        logger.info("allList (게시판 id로 특정 스터디 게시판 전체 글 아이디 리스트 조회)");
        return ResponseEntity.ok().body(studyPostService.allList(studyBoardId));
    }

    // 카테고리 별 게시글 아이디 리스트 조회
    @GetMapping(value = "/studyPosts/category")
    @ApiOperation(value = "카테고리 별 게시글 아이디 리스트 조회", notes = "카테고리 별 게시글 아이디 리스트 조회 API")
    @ApiImplicitParam(name = "category", value = "카테고리 (강의 스터디 게시글, 강의 외 스터디 게시글, 질문 게시글, 자료 공유 게시글, 판매/나눔 게시글)", example = "카테고리")
    public ResponseEntity<List<Long>> findByCategory(@RequestParam String category) {
        logger.info("findByCategory (카테고리 별 게시글 아이디 리스트 조회)");
        return ResponseEntity.ok().body(studyPostService.findByCategory(category));
    }

    // 스터디 게시글 상세 조회
    /*
        스터디 게시글 상세 조회로 글에 포함된 첨부 파일 아이디를 찾은 후, 
        파일 정보를 조회해서 이미지라면 이미지 조회로 이미지를 조회해서 글에 첨부,
        이미지가 아닌 파일이라면 파일 다운로드를 조회해서 글에 첨부
        
     */
    @GetMapping(value = "/studyPost/info")
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글 상세 조회 API")
    @ApiImplicitParam(name = "id", value = "게시글 id", example = "1")
    public ResponseEntity<StudyPostResponseDto> view(@RequestParam Long id) {

        List<StudyFileIdResponseDto> studyFileIdResponseDtoList = studyFilesService.findAllByStudyPost(id);
        List<Long> fileId = new ArrayList<>();

        for (StudyFileIdResponseDto studyFileIdResponseDto : studyFileIdResponseDtoList) {
            fileId.add(studyFileIdResponseDto.getFileId());
        }

        logger.info("view (게시글 상세 조회)");
        return ResponseEntity.ok().body(studyPostService.findById(id, fileId));
    }

    // 파일 정보 조회
    @GetMapping("/studyPost/fileInfo")
    @ApiOperation(value = "파일 id로 이미지/파일 정보 조회", notes = "파일 id로 이미지/파일 정보 조회 API")
    @ApiImplicitParam(name = "id", value = "파일 id", example = "1")
    public ResponseEntity<StudyPostFileResponseDto> findById(@RequestParam Long id) {
        logger.info("findById (파일 id로 이미지/파일 정보 조회)");
        return ResponseEntity.ok().body(studyFilesService.findByFileId(id));
    }

    // 파일 URL 정보 조회
    @GetMapping("/studyPost/fileURL")
    @ApiOperation(value = "파일 id로 이미지/파일 URL 정보 조회", notes = "파일 id로 이미지/파일 URL 정보 조회 API")
    @ApiImplicitParam(name = "id", value = "파일 id", example = "1")
    public ResponseEntity<String> getS3(@RequestParam Long id) {
        StudyFiles studyFiles = studyFilesRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 파일이 없습니다."));
        logger.info("findById (파일 id로 이미지/파일 URL 정보 조회)");
        return ResponseEntity.ok().body(awsS3Service.getS3(studyFiles.getFileName()));
    }

    // 파일 다운로드
    @GetMapping("/studyPost/fileDownload")
    @ApiOperation(value = "파일 id로 이미지/파일 다운로드", notes = "파일 id로 이미지/파일 다운로드 API")
    @ApiImplicitParam(name = "id", value = "파일 id", example = "1")
    public ResponseEntity<byte[]> downloadFiles(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StudyFiles studyFiles = studyFilesRepository.findById(id).orElse(null);
        logger.info("downloadFiles (파일 id로 이미지/파일 다운로드)");
        return awsS3Service.getObject(studyFiles.getOrigFileName(), studyFiles.getFileName());
    }
}
