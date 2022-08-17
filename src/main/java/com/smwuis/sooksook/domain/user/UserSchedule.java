package com.smwuis.sooksook.domain.user;

import com.smwuis.sooksook.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@ToString
@Getter
@NoArgsConstructor
public class UserSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserSchedule_ID")
    private Long id; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private User userId; // 작성자 (fk)

    private Date period; // 기한

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    @Column(nullable = false)
    private boolean finish = false; // 완료 유무

    @Builder
    public UserSchedule(Date period, String content, boolean finish) {
        this.period = period;
        this.content = content;
        this.finish = finish;
    }

    public UserSchedule update(Date period, String content) {
        this.period = period;
        this.content = content;
        return this;
    }

    public void updateFinish() {
        this.finish = !this.finish;
    }

    public void setUser(User user) {
        this.userId = user;

        if(!userId.getUserScheduleList().contains(this))
            user.getUserScheduleList().add(this);
    }
}
