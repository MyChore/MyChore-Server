package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.global.constants.RoomType;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert @DynamicUpdate
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

    @Builder
    public Room (@NonNull Group group, @NonNull Integer sizeX, @NonNull Integer sizeY, @NonNull Integer locationX, @NonNull Integer locationY, @NonNull RoomType roomType, @NonNull String name){
        this.group = group;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.locationX = locationX;
        this.locationY = locationY;
        this.roomType = roomType;
        this.name = name;
    }


}
