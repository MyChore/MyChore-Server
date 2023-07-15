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
public class RoomFurniture extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "room_furn_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "furniture_id")
    private Furniture furniture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @NonNull
    private int locationX;

    @NonNull
    private int locationY;

    @NonNull
    private int rotation;
}
