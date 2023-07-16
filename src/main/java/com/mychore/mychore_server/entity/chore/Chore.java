package com.mychore.mychore_server.entity.chore;

import com.mychore.mychore_server.global.constants.Repetition;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chore extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_furn_id")
    private RoomFurniture roomFurniture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    @Column(length = 20)
    private String name;

    @NonNull
    private LocalDateTime startDate;

    private LocalDateTime lastDate;

    @Enumerated(EnumType.STRING)
    private Repetition repetition;
}
