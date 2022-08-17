package com.smwuis.sooksook.domain.user;

import com.smwuis.sooksook.domain.BaseTimeEntity;

import javax.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id; // 기본키

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String loginId; // 아이디

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(columnDefinition = "TEXT")
    private String introduction; // 한 줄 소개글

    @Column(nullable = false)
    private int points; // 포인트

    @Column(nullable = false)
    private String rating; // 등급

    @Column(nullable = false)
    private boolean withdraw = false; // 탈퇴 여부

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userScheduleList = new ArrayList<>(); // 스케줄 리스트

    @OneToMany(mappedBy = "receiverEmail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRating> userRatingList = new ArrayList<>(); // 평가 리스트
    
    @Builder
    public User(String name, String loginId, String email, String nickname, String password, String introduction, int points, String rating, boolean withdraw) {
        this.name = name;
        this.loginId = loginId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.introduction = introduction;
        this.points = points;
        this.rating = rating;
        this.withdraw = withdraw;
    }

    public User update(String name, String nickname, String introduction) {
        this.name = name;
        this.nickname = nickname;
        this.introduction = introduction;
        return this;
    }

    public void setWithdraw() {
        this.withdraw = true;
        this.nickname = "탈퇴한 회원";
    }

    public User updatePassword(String password) {
        this.password = password;
        return this;
    }

    public void updatePoints(int points) {
        this.points = points + 1;

        if (this.points >= 100) {
            updateRating("눈송이 등급");
        }

        if (this.points >= 200) {
            updateRating("퀸송이 등급");
        }
    }

    public void updateRating(String rating) {
        this.rating = rating;
    }

    public void addUserScheduleList(UserSchedule userSchedule) {
        this.userScheduleList.add(userSchedule);

        if(userSchedule.getUserId() != this) {
            userSchedule.setUser(this);
        }
    }

    public void addUserRatingList(UserRating userRating) {
        this.userRatingList.add(userRating);

        if(userRating.getReceiverEmail() != this) {
            userRating.setReceiverEmail(this);
        }
    }
}