package com.smwuis.sooksook.web.controller.study;

import com.smwuis.sooksook.service.study.StudyMemberService;
import com.smwuis.sooksook.web.dto.study.StudyBoardResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyMemberResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyMemberSaveRequestDto;
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

@Api(tags = "StudyMember API (스터디 게시판 부원 정보 API)")
@RequiredArgsConstructor
@RestController
public class StudyMemberController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final StudyMemberService studyMemberService;

    // 스터디 게시판 비밀번호 확인 및 스터디 가입
    @PostMapping(value = "/studyMember/password")
    @ApiOperation(value = "스터디 게시판 비밀번호 확인 및 스터디 가입", notes = "스터디 게시판 비밀번호 확인 및 스터디 가입 API")
    public ResponseEntity<StudyMemberResponseDto> password(@RequestBody StudyMemberSaveRequestDto saveRequestDto) {
        logger.info("password (스터디 게시판 비밀번호 확인 및 스터디 가입)");
        return ResponseEntity.ok().body(studyMemberService.password(saveRequestDto));
    }

    // 스터디 참여
    @PostMapping(value = "/studyMember")
    @ApiOperation(value = "스터디 게시판 참여", notes = "스터디 게시판 참여 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1"),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일")
    })
    public ResponseEntity<Boolean> join(@RequestParam Long studyBoardId, String email) {
        logger.info("join (스터디 게시판 참여)");
        return ResponseEntity.ok().body(studyMemberService.join(studyBoardId, email));
    }

    // 스터디 탈퇴
    @DeleteMapping(value = "/studyMember")
    @ApiOperation(value = "스터디 게시판 탈퇴", notes = "스터디 게시판 탈퇴 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일"),
            @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1")
    })
    public ResponseEntity<Boolean> drop(@RequestParam String email, Long studyBoardId) {
        logger.info("drop (스터디 게시판 탈퇴)");
        return ResponseEntity.ok().body(studyMemberService.drop(email, studyBoardId));
    }

    // 스터디 부원 정보 (부원, 글 작성 수, 댓글 수)
    @GetMapping(value = "/studyMember")
    @ApiOperation(value = "게시판 id로 스터디 게시판 모든 부원 정보 조회", notes = "게시판 id로 스터디 게시판 모든 부원 정보 조회 API")
    @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1")
    public ResponseEntity<List<StudyMemberResponseDto>> allList(@RequestParam Long studyBoardId) {
        logger.info("allList (게시판 id로 스터디 게시판 모든 부원 정보 조회)");
        return ResponseEntity.ok().body(studyMemberService.findByAllByStudyBoardId(studyBoardId));
    }

    // 내가 참여 중인 스터디
    @GetMapping(value = "/studyMember/myInfo")
    @ApiOperation(value = "내가 참여 중인 스터디 조회", notes = "내가 참여 중인 스터디 조회 API")
    @ApiImplicitParam(name = "email", value = "이메일", example = "1")
    public ResponseEntity<List<StudyBoardResponseDto>> myStudy(@RequestParam String email) {
        logger.info("myStudy (내가 참여 중인 스터디 조회)");
        return ResponseEntity.ok().body(studyMemberService.myStudy(email));
    }

}
