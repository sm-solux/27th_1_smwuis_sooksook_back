package com.smwuis.sooksook.web.controller.study;

import com.smwuis.sooksook.service.study.StudyBoardService;
import com.smwuis.sooksook.web.dto.study.SearchResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyBoardResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyBoardSaveRequestDto;
import com.smwuis.sooksook.web.dto.study.StudyBoardUpdateRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "StudyBoard API (스터디 게시판 API)")
@RequiredArgsConstructor
@RestController
public class StudyBoardController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final StudyBoardService studyBoardService;

    // 강의 스터디 모집 게시판 글 작성
    @PostMapping(value = "/studyBoard/lecture")
    @ApiOperation(value = "강의 스터디 모집 게시판 글 작성", notes = "강의 스터디 모집 게시판 글 작성 API")
    public ResponseEntity<StudyBoardResponseDto> saveLecture(@RequestBody StudyBoardSaveRequestDto saveRequestDto) {
        logger.info("saveLecture (강의 스터디 모집 게시판 글 작성)");
        return ResponseEntity.ok().body(studyBoardService.save(saveRequestDto, true));
    }

    // 강의 외 스터디 모집 게시판 글 작성
    @PostMapping(value = "/studyBoard/notLecture")
    @ApiOperation(value = "강의 외 스터디 모집 게시판 글 작성", notes = "강의 외 스터디 모집 게시판 글 작성 API")
    public ResponseEntity<StudyBoardResponseDto> saveNotLecture(@RequestBody StudyBoardSaveRequestDto saveRequestDto) {
        logger.info("saveNotLecture (강의 외 스터디 모집 게시판 글 작성)");
        return ResponseEntity.ok().body(studyBoardService.save(saveRequestDto, false));
    }

    // 스터디 모집 게시판 글 수정
    @PutMapping(value = "/studyBoard")
    @ApiOperation(value = "스터디 모집 게시판 글 수정", notes = "스터디 모집 게시판 글 수정 API")
    @ApiImplicitParam(name = "id", value = "게시판 id", example = "1")
    public ResponseEntity<StudyBoardResponseDto> update(@RequestParam Long id, @RequestBody StudyBoardUpdateRequestDto updateRequestDto) {
        logger.info("update (스터디 모집 게시판 글 수정)");
        return ResponseEntity.ok().body(studyBoardService.update(id, updateRequestDto));
    }

    // 스터디 모집 게시판 글 삭제
    @DeleteMapping(value = "/studyBoard")
    @ApiOperation(value = "스터디 모집 게시판 글 삭제", notes = "스터디 모집 게시판 글 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "게시판 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일"),
    })
    public ResponseEntity<Boolean> delete(@RequestParam Long id, String email) {
        logger.info("delete (스터디 모집 게시판 글 삭제)");
        return ResponseEntity.ok().body(studyBoardService.delete(id, email));
    }

    // 스터디 종료
    @PutMapping(value = "/studyBoard/finish")
    @ApiOperation(value = "스터디 종료", notes = "스터디 종료 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "게시판 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일"),
    })
    public ResponseEntity<Boolean> finish(@RequestParam Long id, String email) {
        logger.info("finish (스터디 종료)");
        return ResponseEntity.ok().body(studyBoardService.finish(id, email));
    }

    // 스터디 모집 게시판 강의 스터디 / 강의 외 스터디 글 전체 리스트 조회
    @GetMapping(value = "/studyBoards/list")
    @ApiOperation(value = "스터디 모집 게시판 강의 스터디 / 강의 외 스터디 글 전체 리스트 조회", notes = "스터디 모집 게시판 강의 스터디 / 강의 외 스터디 글 전체 리스트 조회 API")
    @ApiImplicitParam(name = "lecture", value = "true면 스터디 게시판, false면 스터디 외 게시판", example = "true")
    public ResponseEntity<List<StudyBoardResponseDto>> studyList(@RequestParam Boolean lecture) {
        logger.info("studyList (스터디 모집 게시판 강의 스터디 / 강의 외 스터디 글 전체 리스트 조회)");
        return ResponseEntity.ok().body(studyBoardService.studyList(lecture));
    }

    // 스터디 모집 게시판 글 상세 조회
    @GetMapping(value = "/studyBoard")
    @ApiOperation(value = "게시판 id 값으로 스터디 모집 게시판 글 하나 상세 조회", notes = "게시판 id 값으로 스터디 모집 게시판 글 하나 상세 조회 API")
    @ApiImplicitParam(name = "id", value = "게시판 id", example = "1")
    public ResponseEntity<StudyBoardResponseDto> view(@RequestParam Long id) {
        logger.info("view (게시판 id 값으로 스터디 모집 게시판 글 하나 상세 조회)");
        return ResponseEntity.ok().body(studyBoardService.findById(id));
    }
    
    // 스터디 게시판 강의 스터디 학부 별 검색
    @GetMapping(value = "/studyBoards/department")
    @ApiOperation(value = "스터디 모집 게시판 강의 스터디 학부 별 리스트 조회", notes = "스터디 모집 게시판 강의 스터디 학부 별 리스트 조회 API")
    @ApiImplicitParam(name = "department", value = "학부 이름", example = "학부 이름")
    public ResponseEntity<List<StudyBoardResponseDto>> departmentList(@RequestParam String department) {
        logger.info("departmentList (스터디 모집 게시판 강의 스터디 학부 별 리스트 조회)");
        return ResponseEntity.ok().body(studyBoardService.departmentList(department));
    }

    // 스터디 게시판 강의 외 스터디 카테고리 별 검색
    @GetMapping(value = "/studyBoards/category")
    @ApiOperation(value = "스터디 모집 게시판 강의 외 스터디 카테고리 별 리스트 조회", notes = "스터디 모집 게시판 강의 외 스터디 카테고리 별 리스트 조회 API")
    @ApiImplicitParam(name = "category", value = "카테고리", example = "카테고리")
    public ResponseEntity<List<StudyBoardResponseDto>> categoryList(@RequestParam String category) {
        logger.info("categoryList (스터디 모집 게시판 강의 외 스터디 카테고리 별 리스트 조회)");
        return ResponseEntity.ok().body(studyBoardService.categoryList(category));
    }

    // 일주일간 댓글이 많이 달린 인기 스터디 5개 조회
    @GetMapping(value = "/studyBoard/famous")
    @ApiOperation(value = "일주일간 댓글이 많이 달린 인기 스터디 5개 조회", notes = "일주일간 댓글이 많이 달린 인기 스터디 5개 조회 API")
    public ResponseEntity<List<Map<String, Object>>> famousList() {
        logger.info("famousList (일주일간 댓글이 많이 달린 인기 스터디 5개 조회)");
        return ResponseEntity.ok().body(studyBoardService.famousList());
    }

    // 새로운 스터디 5개 조회
    @GetMapping(value = "/studyBoard/new")
    @ApiOperation(value = "새로운 스터디 5개 조회", notes = "새로운 스터디 5개 조회 API")
    public ResponseEntity<List<Map<String, Object>>> newList() {
        logger.info("newList (새로운 스터디 5개 조회)");
        return ResponseEntity.ok().body(studyBoardService.newList());
    }

    // 스터디 게시판에 글이 많아 참여도 높은 스터디
    @GetMapping(value = "/studyBoard/hard")
    @ApiOperation(value = "참여도 높은 스터디 5개 조회", notes = "참여도 높은 스터디 5개 조회 API")
    public ResponseEntity<List<Map<String, Object>>> hardList() {
        logger.info("hardList (참여도 높은 스터디 5개 조회)");
        return ResponseEntity.ok().body(studyBoardService.hardList());
    }

    // 스터디 모집 게시판 및 게시글 제목 검색
    @GetMapping(value = "/studyBoard/search")
    @ApiOperation(value = "스터디 모집 게시판 및 게시글 제목 검색", notes = "스터디 모집 게시판 및 게시글 제목 검색 API")
    @ApiImplicitParam(name = "keyword", value = "검색할 키워드", example = "제목")
    public ResponseEntity<List<SearchResponseDto>> searchKeyword(@RequestParam String keyword) {
        logger.info("searchKeyword (스터디 모집 게시판 및 게시글 제목 검색)");
        return ResponseEntity.ok().body(studyBoardService.searchKeyword(keyword));
    }
}