package com.smwuis.sooksook.web.controller.user;

import com.smwuis.sooksook.service.user.UserRatingService;
import com.smwuis.sooksook.web.dto.user.UserRatingResponseDto;
import com.smwuis.sooksook.web.dto.user.UserRatingTotalResponseDto;
import com.smwuis.sooksook.web.dto.user.UserRatingSaveRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "UserRating API (유저 평가 API)")
@RequiredArgsConstructor
@RestController
public class UserRatingController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRatingService userRatingService;

    // 유저 평가 생성
    @PostMapping("/userRating")
    @ApiOperation(value = "유저 평가 등록", notes = "유저 평가 등록 API")
    public ResponseEntity<UserRatingResponseDto> save(@RequestBody UserRatingSaveRequestDto saveRequestDto) {
        logger.info("save (유저 평가 등록)");
        return ResponseEntity.ok().body(userRatingService.save(saveRequestDto));
    }

    // 유저 평가 수정
    @PutMapping("/userRating")
    @ApiOperation(value = "유저 평가 수정", notes = "유저 평가 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "유저 평가 id", example = "1", required = true),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true),
            @ApiImplicitParam(name = "contents", value = "평가 내용", example = "성실해요", required = true),
            @ApiImplicitParam(name = "score", value = "평가 별점", example = "4.0", required = true)
    })
    public ResponseEntity<UserRatingResponseDto> update(@RequestParam Long id, String email, String contents, float score) {
        logger.info("update (유저 평가 수정)");
        return ResponseEntity.ok().body(userRatingService.update(id, email, contents, score));
    }

    // 유저 평가 삭제
    @DeleteMapping("/userRating")
    @ApiOperation(value = "유저 평가 삭제", notes = "유저 평가 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "유저 평가 id", example = "1", required = true),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true),
    })    public ResponseEntity<Boolean> delete(@RequestParam Long id, String email) {
        logger.info("delete (유저 평가 삭제)");
        return ResponseEntity.ok().body(userRatingService.delete(id, email));
    }

    // 나의 모든 유저 평가 조회
    @GetMapping("/userRating/myInfo")
    @ApiOperation(value = "나에 대한 모든 유저 평가 조회 전체 조회", notes = "나에 대한 모든 유저 평가 조회 전체 조회 API")
    @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true)
    public ResponseEntity<List<UserRatingResponseDto>> findMyRating(@RequestParam String email) {
        logger.info("findMyRating (나에 대한 모든 유저 평가 조회 전체 조회)");
        return ResponseEntity.ok().body(userRatingService.findMyRating(email));
    }

    // 유저 평가 상세 조회
    @GetMapping("/userRating/info")
    @ApiOperation(value = "유저 평가 id로 유저 평가 하나 상세 조회", notes = "유저 평가 id로 유저 평가 하나 상세 조회 API")
    @ApiImplicitParam(name = "id", value = "유저 평가 id", example = "1", required = true)
    public ResponseEntity<UserRatingResponseDto> findRating(@RequestParam Long id) {
        logger.info("findRating (유저 평가 id로 유저 평가 하나 상세 조회)");
        return ResponseEntity.ok().body(userRatingService.findRating(id));
    }

    // 유저 평가 스터디별 종합 조회
    @GetMapping("/userRating/total")
    @ApiOperation(value = "스터디 게시판 id로 나의 스터디 총 평가 조회", notes = "스터디 게시판 id로 나의 스터디 총 평가 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true),
            @ApiImplicitParam(name = "studyBoardId", value = "게시판 id", example = "1", required = true),
    })
    public ResponseEntity<UserRatingTotalResponseDto> findRatingWithStudyBoard(@RequestParam String email, Long studyBoardId) {
        logger.info("findRatingWithStudyBoard (스터디 게시판 id로 나의 스터디 총 평가 조회)");
        return ResponseEntity.ok().body(userRatingService.findRatingWithStudyBoard(email, studyBoardId));
    }
}
