package com.smwuis.sooksook.web.controller.study;

import com.smwuis.sooksook.service.study.StudyScheduleService;
import com.smwuis.sooksook.web.dto.study.StudyScheduleResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyScheduleSaveRequestDto;
import com.smwuis.sooksook.web.dto.study.StudyScheduleUpdateRequestDto;
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

@Api(tags = "StudySchedule API (스터디 게시판 스케줄 정보 API)")
@RequiredArgsConstructor
@RestController
public class StudyScheduleController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final StudyScheduleService studyScheduleService;

    // 스터디 게시판 스케줄 추가
    @PostMapping(value = "/studySchedule")
    @ApiOperation(value = "스터디 게시판 스케줄 작성", notes = "스터디 게시판 스케줄 작성 API")
    public ResponseEntity<StudyScheduleResponseDto> save(@RequestBody StudyScheduleSaveRequestDto saveRequestDto) {
        logger.info("save (스터디 게시판 스케줄 작성)");
        return ResponseEntity.ok().body(studyScheduleService.save(saveRequestDto));
    }

    // 스터디 게시판 스케줄 수정
    @PutMapping(value = "/studySchedule")
    @ApiOperation(value = "스터디 게시판 스케줄 수정", notes = "스터디 게시판 스케줄 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1"),
            @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1")
    })
    public ResponseEntity<StudyScheduleResponseDto> update(@RequestParam Long id, Long studyBoardId, @RequestBody StudyScheduleUpdateRequestDto updateDto) {
        logger.info("update (스터디 게시판 스케줄 수정)");
        return ResponseEntity.ok().body(studyScheduleService.update(id, studyBoardId, updateDto));
    }

    // 스터디 게시판 스케줄 삭제
    @DeleteMapping(value = "studySchedule")
    @ApiOperation(value = "스터디 게시판 스케줄 삭제", notes = "스터디 게시판 스케줄 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1"),
            @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1")
    })
    public ResponseEntity<Boolean> delete(@RequestParam Long id, Long studyBoardId) {
        logger.info("delete (스터디 게시판 스케줄 삭제)");
        return ResponseEntity.ok().body(studyScheduleService.delete(id, studyBoardId));
    }

    // 스터디 게시판 스케줄 전체 리스트 조회
    @GetMapping(value = "/studySchedules/all")
    @ApiOperation(value = "게시판 id로 스터디 게시판 스케줄 리스트 전체 조회", notes = "게시판 id로 스터디 게시판 스케줄 리스트 전체 조회 API")
    @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1")
    public ResponseEntity<List<StudyScheduleResponseDto>> allList(@RequestParam Long studyBoardId) {
        logger.info("allList (게시판 id로 스터디 게시판 스케줄 리스트 전체 조회)");
        return ResponseEntity.ok().body(studyScheduleService.allList(studyBoardId));
    }

    // 스터디 게시판 스케줄 개별 조회
    @GetMapping(value = "/studySchedule")
    @ApiOperation(value = "스케줄 id로 스터디 게시판 스케줄 상세 조회", notes = "스케줄 id로 스터디 게시판 스케줄 상세 조회")
    @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1")
    public ResponseEntity<StudyScheduleResponseDto> view(@RequestParam Long id) {
        logger.info("view (스케줄 id로 스터디 게시판 스케줄 상세 조회)");
        return ResponseEntity.ok().body(studyScheduleService.findByIdAndStudyBoardId(id));
    }

}
