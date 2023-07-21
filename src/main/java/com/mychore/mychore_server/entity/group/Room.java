package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.global.constants.RoomType;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    @Column(length = 10)
    private String name;

    @NonNull
    @Column(name = "size_x")
    private Integer sizeX;

    @NonNull
    @Column(name = "size_y")
    private Integer sizeY;

    @NonNull
    @Column(name = "location_x")
    private Integer locationX;

    @NonNull
    @Column(name = "location_y")
    private Integer locationY;

    @NonNull
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
}
