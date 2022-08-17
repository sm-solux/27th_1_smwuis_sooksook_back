package com.smwuis.sooksook.web.controller.user;

import com.smwuis.sooksook.service.user.UserScheduleService;
import com.smwuis.sooksook.web.dto.user.UserScheduleRequestDto;
import com.smwuis.sooksook.web.dto.user.UserScheduleResponseDto;
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

@Api(tags = "UserSchedule API (유저 스케줄 API)")
@RequiredArgsConstructor
@RestController
public class UserScheduleController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserScheduleService userScheduleService;

    // 스케줄 등록
    @PostMapping("/userSchedule")
    @ApiOperation(value = "유저 스케줄 등록", notes = "유저 스케줄 등록 API")
    public ResponseEntity<UserScheduleResponseDto> save(@RequestBody UserScheduleRequestDto requestDto) {
        logger.info("save (유저 스케줄 등록)");
        return ResponseEntity.ok().body(userScheduleService.save(requestDto));
    }

    // 스케줄 수정
    @PutMapping("/userSchedule")
    @ApiOperation(value = "유저 스케줄 수정", notes = "유저 스케줄 수정 API")
    @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1", required = true)
    public ResponseEntity<UserScheduleResponseDto> update(@RequestParam Long id, @RequestBody UserScheduleRequestDto requestDto) {
        logger.info("update (유저 스케줄 수정)");
        return ResponseEntity.ok().body(userScheduleService.update(id, requestDto));
    }

    // 스케줄 삭제
    @DeleteMapping("/userSchedule")
    @ApiOperation(value = "유저 스케줄 삭제", notes = "유저 스케줄 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1", required = true),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true)
    })
    public ResponseEntity<Boolean> delete(@RequestParam Long id, String email) {
        logger.info("delete (유저 스케줄 삭제)");
        return ResponseEntity.ok().body(userScheduleService.delete(id, email));
    }

    // 스케줄 완료 체크
    @PutMapping("/userSchedule/check")
    @ApiOperation(value = "유저 스케줄 완료/미완료 체크", notes = "유저 스케줄 완료/미완료 체크 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1", required = true),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true)
    })
    public ResponseEntity<UserScheduleResponseDto> finish(@RequestParam Long id, String email) {
        logger.info("finish (유저 스케줄 완료/미완료 체크)");
        return ResponseEntity.ok().body(userScheduleService.finish(id, email));
    }

    // 나의 스케줄 조회
    @GetMapping("/userSchedule/myInfo")
    @ApiOperation(value = "나의 스케줄 조회", notes = "나의 스케줄 조회 API")
    @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true)
    public ResponseEntity<List<UserScheduleResponseDto>> findMySchedule(@RequestParam String email) {
        logger.info("findMySchedule (나의 스케줄 조회)");
        return ResponseEntity.ok().body(userScheduleService.findMySchedule(email));
    }

    // 스케줄 상세 조회
    @GetMapping("/userSchedule/info")
    @ApiOperation(value = "스케줄 id로 스케줄 하나 상세 조회", notes = "스케줄 id로 스케줄 하나 상세 조회 API")
    @ApiImplicitParam(name = "id", value = "스케줄 id", example = "1", required = true)
    public ResponseEntity<UserScheduleResponseDto> findSchedule(@RequestParam Long id) {
        logger.info("findSchedule (스케줄 id로 스케줄 하나 상세 조회)");
        return ResponseEntity.ok().body(userScheduleService.findSchedule(id));
    }
}
