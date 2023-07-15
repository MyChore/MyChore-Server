package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    private int sizeX;

    @NonNull
    private int sizeY;

    @NonNull
    private int locationX;

    @NonNull
    private int locationY;

    @NonNull
    private int rotation;

    @Enumerated(EnumType.STRING)
    @NonNull
    private RoomType roomType;
}
