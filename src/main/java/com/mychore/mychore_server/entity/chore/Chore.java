package com.mychore.mychore_server.entity.chore;

import com.mychore.mychore_server.global.constants.Repetition;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chore_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_furn_id")
    private RoomFurniture roomFurniture;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    @Column(length = 20)
    private String name;

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAcceptNoti = true;

    @NonNull
    private LocalDateTime startDate;

    private LocalDateTime lastDate;

    @Enumerated(EnumType.STRING)
    private Repetition repetition;
}
