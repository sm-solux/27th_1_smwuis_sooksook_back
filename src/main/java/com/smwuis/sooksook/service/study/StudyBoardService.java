package com.smwuis.sooksook.service.study;

import com.smwuis.sooksook.domain.study.*;
import com.smwuis.sooksook.domain.user.User;
import com.smwuis.sooksook.domain.user.UserRepository;
import com.smwuis.sooksook.web.dto.study.SearchResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyBoardResponseDto;
import com.smwuis.sooksook.web.dto.study.StudyBoardSaveRequestDto;
import com.smwuis.sooksook.web.dto.study.StudyBoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyBoardService {

    private final StudyBoardRepository studyBoardRepository;
    private final UserRepository userRepository;
    private final PasswordCommentRepository passwordCommentRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final StudyPostRepository studyPostRepository;

    private final LocalDateTime startDatetime = LocalDateTime.now();
    private final LocalDateTime endDatetime = LocalDateTime.now().plusDays(7);

    // 강의 스터디 모집 게시판 글 작성
    @Transactional
    public StudyBoardResponseDto save(StudyBoardSaveRequestDto saveRequestDto, boolean tf) {
        StudyBoard studyBoard = saveRequestDto.toEntity();
        studyBoard.setLecture(tf);
        studyBoard.setUser(userRepository.findByEmail(saveRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다.")));
        studyBoardRepository.save(studyBoard);

        return new StudyBoardResponseDto(studyBoard);
    }

    // 스터디 모집 게시판 글 수정
    @Transactional
    public StudyBoardResponseDto update(Long id, StudyBoardUpdateRequestDto updateRequestDto) {
        User user = userRepository.findByEmail(updateRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        if(user.equals(studyBoard.getUserId())) {
            studyBoard.update(updateRequestDto.getDepartment(),
                    updateRequestDto.getSubject(),
                    updateRequestDto.getTitle(),
                    updateRequestDto.getContent(),
                    updateRequestDto.getNumber(),
                    updateRequestDto.getOnoff(),
                    updateRequestDto.getPeriod(),
                    updateRequestDto.getPassword(),
                    updateRequestDto.isLecture(),
                    updateRequestDto.getCategory());
            return new StudyBoardResponseDto(studyBoard);
        }
        else {
            throw new RuntimeException("스터디 게시판 수정에 실패했습니다.");
        }
    }

    // 스터디 모집 게시판 글 삭제
    @Transactional
    public Boolean delete(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        if(user.equals(studyBoard.getUserId())) {
            studyBoardRepository.delete(studyBoard);
            return true;
        }
        else {
            throw new RuntimeException("스터디 게시판 삭제에 실패했습니다.");
        }
    }

    // 스터디 종료
    @Transactional
    public Boolean finish(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));

        if(user.equals(studyBoard.getUserId())) {
            studyBoard.setFinished();
            return true;
        }
        else {
            throw new RuntimeException("스터디 종료에 실패했습니다.");
        }
    }

    // 스터디 모집 게시판 강의 스터디 / 강의 외 스터디 글 전체 리스트 조회
    @Transactional
    public List<StudyBoardResponseDto> studyList(Boolean lecture) {
        return studyBoardRepository.findByLecture(lecture)
                .stream()
                .map(StudyBoardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 스터디 모집 게시판 글 상세 조회
    @Transactional
    public StudyBoardResponseDto findById(Long id) {
        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        return new StudyBoardResponseDto(studyBoard);
    }

    // 스터디 게시판 강의 스터디 학부 별 검색
    @Transactional
    public List<StudyBoardResponseDto> departmentList(String department) {
        return studyBoardRepository.findByLectureAndDepartment(true, department)
                .stream()
                .map(StudyBoardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 스터디 게시판 강의 외 스터디 카테고리 별 검색
    @Transactional
    public List<StudyBoardResponseDto> categoryList(String category) {
        return studyBoardRepository.findByLectureAndCategory(false, category)
                .stream()
                .map(StudyBoardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 일주일간 게시판에 달린 댓글 갯수
    @Transactional(readOnly = true)
    public Long countPasswordComments(Long id){
        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        return passwordCommentRepository.countByCreatedDateTimeBetweenAndStudyBoardId(startDatetime, endDatetime, studyBoard);
    }

    // 일주일간 스터디 게시판에 댓글이 많이 달린 인기 스터디 5개 조회
    @Transactional(readOnly = true)
    public List<Map<String, Object>> famousList() {

        List<StudyBoard> studyBoardList = studyBoardRepository.findAll();
        List<Map<String, Object>> countList = new ArrayList<>();

        for (StudyBoard studyBoard: studyBoardList) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", studyBoard.getTitle());
            map.put("studyBoardId", studyBoard.getId());
            map.put("lecture", setLecture(studyBoard));
            map.put("countComment", countPasswordComments(studyBoard.getId()));

            countList.add(map);
        }

        Collections.sort(countList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> obj1, Map<String, Object> obj2) {
                Long cnt1 = (Long) obj1.get("countComment");
                Long cnt2 = (Long) obj2.get("countComment");
                return cnt2.compareTo(cnt1);
            }
        });

        if(countList.size() < 5){
            return countList.subList(0, countList.size());
        } else {
            return countList.subList(0, 5);
        }
    }

    // 새로운 스터디 5개 조회
    @Transactional(readOnly = true)
    public List<Map<String, Object>> newList() {

        List<StudyBoard> studyBoardList = studyBoardRepository.findTop5ByOrderByIdDesc();
        List<Map<String, Object>> newList = new ArrayList<>();

        for (StudyBoard studyBoard: studyBoardList) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", studyBoard.getTitle());
            map.put("studyBoardId", studyBoard.getId());
            map.put("lecture", setLecture(studyBoard));

            newList.add(map);
        }

        if(newList.size() < 5){
            return newList.subList(0, newList.size());
        } else {
            return newList.subList(0, 5);
        }
    }

    // 일주일간 게시판에 달린 게시글 갯수
    @Transactional(readOnly = true)
    public Long countStudyPosts(Long id) {
        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        return studyPostRepository.countByCreatedDateTimeBetweenAndStudyBoardId(startDatetime, endDatetime, studyBoard);
    }

    // 일주일간 게시판의 게시글에 달린 댓글 갯수
    @Transactional(readOnly = true)
    public Long countStudyComments(Long id){
        Long count = 0L;

        StudyBoard studyBoard = studyBoardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시판이 없습니다."));
        List<StudyPost> studyPostList = studyPostRepository.findAllByStudyBoardId(studyBoard);

        List<Long> studyCommentList = new ArrayList<>();

        // 게시글마다 댓글 갯수 조회
        for(StudyPost studyPost: studyPostList) {
            studyCommentList.add(studyCommentRepository.countByCreatedDateTimeBetweenAndStudyPostId(startDatetime, endDatetime, studyPost));
        }

        for(int i = 0; i < studyCommentList.size(); i++) {
            count += studyCommentList.get(i);
        }

        return count;

    }

    // 일주일간 스터디 게시판에 글과 댓글이 많아 참여도 높은 스터디 5개 조회
    @Transactional(readOnly = true)
    public List<Map<String, Object>> hardList() {

        List<StudyBoard> studyBoardList = studyBoardRepository.findAll();
        List<Map<String, Object>> countList = new ArrayList<>();

        for (StudyBoard studyBoard: studyBoardList) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", studyBoard.getTitle());
            map.put("studyBoardId", studyBoard.getId());
            map.put("lecture", setLecture(studyBoard));
            map.put("countPostsAndComment", countStudyPosts(studyBoard.getId()) + countStudyComments(studyBoard.getId()));

            countList.add(map);
        }

        Collections.sort(countList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> obj1, Map<String, Object> obj2) {
                Long cnt1 = (Long) obj1.get("countPostsAndComment");
                Long cnt2 = (Long) obj2.get("countPostsAndComment");
                return cnt2.compareTo(cnt1);
            }
        });

        if(countList.size() < 5){
            return countList.subList(0, countList.size());
        } else {
            return countList.subList(0, 5);
        }
    }

    // 스터디 모집 게시판 및 게시글 제목 검색
    @Transactional
    public List<SearchResponseDto> searchKeyword(String keyword) {
        List<StudyBoard> studyBoardList = studyBoardRepository.findByTitleContaining(keyword);
        List<StudyPost> studyPostList = studyPostRepository.findByTitleContaining(keyword);
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();

        for(StudyBoard studyBoard: studyBoardList) {
            searchResponseDtoList.add(new SearchResponseDto(studyBoard));
        }

        for(StudyPost studyPost : studyPostList) {
            searchResponseDtoList.add(new SearchResponseDto(studyPost));
        }

        return searchResponseDtoList
                .stream()
                .sorted(Comparator.comparing(SearchResponseDto::getCreatedDateTime))
                .collect(Collectors.toList());
    }

    public String setLecture(StudyBoard studyBoard) {
        if(studyBoard.isLecture()) {
            return "강의 스터디";
        }
        else {
            return  "강의 외 스터디";
        }
    }
}
