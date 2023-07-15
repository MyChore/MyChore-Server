package com.mychore.mychore_server.entity.user;

import com.mychore.mychore_server.constant.Provider;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NonNull
    @Column(length = 100)
    private String email;

    @NonNull
    @Column(length = 10)
    private String nickname;

    @Column(length = 10)
    private String gender;

    private LocalDateTime birth;

    private String profileImgKey;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Provider provider;
}
