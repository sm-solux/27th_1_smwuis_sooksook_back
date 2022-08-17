package com.smwuis.sooksook.web.controller.user;

import com.smwuis.sooksook.service.user.UserService;
import com.smwuis.sooksook.web.dto.user.UserResponseDto;
import com.smwuis.sooksook.web.dto.user.UserSaveRequestDto;
import com.smwuis.sooksook.web.dto.user.UserUpdateRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "User API (유저 API)")
@RequiredArgsConstructor
@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    // 유저 생성 (회원 가입)
    @PostMapping("/user")
    @ApiOperation(value = "유저 생성 (회원가입)", notes = "유저 생성 (회원가입) API")
    public ResponseEntity<UserResponseDto> singUp(@RequestBody UserSaveRequestDto saveRequestDto) {
        logger.info("singUp (유저 생성)");
        return ResponseEntity.ok().body(userService.signup(saveRequestDto));
    }

    // 유저 수정
    @PutMapping("/user")
    @ApiOperation(value = "유저 수정", notes = "유저 수정 API")
    @ApiImplicitParam(name = "id", value = "유저 id", example = "1", required = true)
    public ResponseEntity<UserResponseDto> update(@RequestParam Long id, @RequestBody UserUpdateRequestDto updateRequestDto) {
        logger.info("update (유저 수정)");
        return ResponseEntity.ok(userService.update(id, updateRequestDto));
    }

    // 유저 비밀번호 수정
    @PutMapping("/user/password")
    @ApiOperation(value = "유저 비밀번호 수정", notes = "유저 비밀번호 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true),
            @ApiImplicitParam(name = "oldPassword", value = "현재 비밀번호", example = "현재 비밀번호", required = true),
            @ApiImplicitParam(name = "newPassword", value = "새 비밀번호", example = "새 비밀번호", required = true)
    })
    public ResponseEntity<UserResponseDto> updatePassword(@RequestParam String email, String oldPassword, String newPassword) {
        logger.info("updatePassword (유저 비밀번호 수정)");
        return ResponseEntity.ok().body(userService.updatePassword(email, oldPassword, newPassword));
    }

    // 유저 탈퇴
    @DeleteMapping("/user")
    @ApiOperation(value = "유저 탈퇴", notes = "유저 탈퇴 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "유저 id", example = "1", required = true),
            @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true)
    })
    public ResponseEntity<Boolean> delete(@RequestParam Long id, String email) {
        logger.info("delete (유저 탈퇴)");
        return ResponseEntity.ok().body(userService.delete(id, email));
    }

    // 로그인
    @GetMapping("/user")
    @ApiOperation(value = "로그인", notes = "로그인 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "유저 로그인 아이디", example = "아이디", required = true),
            @ApiImplicitParam(name = "password", value = "비밀번호", example = "비밀번호", required = true)
    })
    public ResponseEntity<UserResponseDto> login(@RequestParam String loginId, String password) {
        logger.info("login (로그인)");
        return ResponseEntity.ok().body(userService.login(loginId, password));
    }

    // 유저 정보 조회
    @GetMapping("/user/myInfo")
    @ApiOperation(value = "이메일로 유저 정보 조회", notes = "이메일로 유저 정보 조회 API")
    @ApiImplicitParam(name = "email", value = "이메일", example = "이메일", required = true)
    public ResponseEntity<UserResponseDto> findUser(@RequestParam String email) {
        logger.info("findUser (이메일로 유저 정보 조회)");
        return ResponseEntity.ok().body(userService.findUser(email));
    }
}
