package com.mychore.mychore_server.entity.user;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAgree extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    private Boolean ageStatus = false;

    private LocalDateTime promotion;

    @NonNull
    private Boolean todayNoti = false;

    @NonNull
    private Boolean newUserNoti = false;

    @NonNull
    private Boolean doneNoti = false;

    private Boolean deleteNoti = false;
}
