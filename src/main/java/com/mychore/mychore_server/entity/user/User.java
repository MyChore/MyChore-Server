package com.mychore.mychore_server.entity.user;

import com.mychore.mychore_server.global.constants.Provider;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private LocalDate birth;

    private String imgKey;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Provider provider;
}
