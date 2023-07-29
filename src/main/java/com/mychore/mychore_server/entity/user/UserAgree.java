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

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAcceptTodayNoti = true;

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAcceptNewUserNoti = true;

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAcceptDoneNoti = true;

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAcceptDeleteNoti = true;

    @Builder
    public UserAgree(@NonNull User user, @NonNull Boolean is14Over, LocalDateTime acceptEmailDate) {
        this.user = user;
        this.is14Over = is14Over;
        this.acceptEmailDate = acceptEmailDate;
    }
}
