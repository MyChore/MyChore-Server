package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.dto.Group.Req.InfoList.FurnitureInfoDTO;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert @DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomFurniture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_furn_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "furniture_id")
    private Furniture furniture;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @NonNull
    @Column(name = "location_x")
    private Integer locationX;

    @NonNull
    @Column(name = "location_y")
    private Integer locationY;

    @NonNull
    private Integer rotation;

    @Builder
    public RoomFurniture(@NonNull Room room, @NonNull Furniture furniture, @NonNull Integer locationX, @NonNull Integer locationY, @NonNull Integer rotation){
        this.room = room;
        this.furniture = furniture;
        this.locationX = locationX;
        this.locationY = locationY;
        this.rotation = rotation;
    }
}
