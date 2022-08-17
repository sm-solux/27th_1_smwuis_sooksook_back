package com.smwuis.sooksook.web.controller.study;

import com.smwuis.sooksook.service.study.StudyCommentService;
import com.smwuis.sooksook.web.dto.study.StudyCommentResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyCommentSaveRequestDto;
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

@Api(tags = "StudyComment API (게시글 댓글 API)")
@RequiredArgsConstructor
@RestController
public class StudyCommentController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final StudyCommentService studyCommentService;

    // 댓글 작성
    @PostMapping(value = "/studyComment")
    @ApiOperation(value = "게시글 댓글 작성", notes = "게시글 댓글 작성 API")
    public ResponseEntity<StudyCommentResponseDto> save(@RequestBody StudyCommentSaveRequestDto saveRequestDto) {
        logger.info("save (게시글 댓글 작성)");
        return ResponseEntity.ok().body(studyCommentService.save(saveRequestDto));
    }
    
    // 댓글 수정
    @PutMapping(value = "/studyComment")
    @ApiOperation(value = "게시글 댓글 수정", notes = "게시글 댓글 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "댓글 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일"),
            @ApiImplicitParam(name = "content", value = "댓글 내용", example = "내용"),
    })
    public ResponseEntity<StudyCommentResponseDto> update(@RequestParam Long id, String email, String content) {
        logger.info("update (게시글 댓글 수정)");
        return ResponseEntity.ok().body(studyCommentService.update(id, email, content));
    }
    
    // 댓글 삭제
    @DeleteMapping(value = "/studyComment")
    @ApiOperation(value = "게시글 댓글 삭제", notes = "게시글 댓글 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "댓글 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일")
    })
    public ResponseEntity<Boolean> delete(@RequestParam Long id, String email) {
        logger.info("delete (게시글 댓글 삭제)");
        return ResponseEntity.ok().body(studyCommentService.delete(id, email));
    }
    
    // 댓글 전체 조회
    @GetMapping(value = "/studyComments/all")
    @ApiOperation(value = "게시글 id로 게시글 댓글 전체 조회 (대댓글 제외, 대댓글은 댓글의 childList 안에)", notes = "게시글 댓글 전체 조회 (대댓글 제외) API")
    @ApiImplicitParam(name = "studyPostId", value = "게시글 id", example = "1")
    public ResponseEntity<List<StudyCommentResponseDto>> allList(@RequestParam Long studyPostId) {
        logger.info("allList (게시글 id로 게시글 댓글 전체 조회)");
        return ResponseEntity.ok().body(studyCommentService.allList(studyPostId));
    }

    // 댓글 상세 조회
    @GetMapping(value = "/studyComment/info")
    @ApiOperation(value = "댓글 id로 게시글 댓글 하나 상세 조회", notes = "게시글 id로 게시글 댓글 하나 상세 조회 API")
    @ApiImplicitParam(name = "id", value = "댓글 id", example = "1")
    public ResponseEntity<StudyCommentResponseDto> view(@RequestParam Long id) {
        logger.info("view (댓글 id로 게시글 댓글 하나 상세 조회)");
        return ResponseEntity.ok().body(studyCommentService.view(id));
    }
}
