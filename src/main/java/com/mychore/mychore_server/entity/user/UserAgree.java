package com.mychore.mychore_server.entity.user;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Getter
public class UserAgree extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_agree_id")
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @Column(name = "is_14_over", columnDefinition = "BOOLEAN")
    private Boolean is14Over;

    private LocalDateTime acceptEmailDate;

    // 집안일 알림
    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    @Setter
    private Boolean isAgreeChoreNoti = true;

    // 오늘의 집안일 알림
    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    @Setter
    private Boolean isAgreeTodayNoti = true;

    // 그룹원 추가 알림
    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    @Setter
    private Boolean isAgreeNewUserNoti = true;

    // 그룹원의 집안일 완료 알림
    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    @Setter
    private Boolean isAgreeDoneNoti = true;

    // 그룹 삭제 알림
    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    @Setter
    private Boolean isAgreeDeleteNoti = true;

    @Builder
    public UserAgree(@NonNull User user, @NonNull Boolean is14Over, LocalDateTime acceptEmailDate) {
        this.user = user;
        this.is14Over = is14Over;
        this.acceptEmailDate = acceptEmailDate;
    }
}
