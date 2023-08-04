package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.global.constants.FurnitureType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert @DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Furniture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furniture_id")
    private Long id;

    @NonNull
    @Column(name = "size_x")
    private Integer sizeX;

    @NonNull
    @Column(name = "size_y")
    private Integer sizeY;

    @NonNull
    @Column(length = 10)
    private String name;

    private String imgKey;

    @NonNull
    @Enumerated(EnumType.STRING)
    private FurnitureType furnitureType;

    @Builder
    public Furniture(@NonNull String name, @NonNull Integer sizeX, @NonNull Integer sizeY, String imgKey, FurnitureType furnitureType){
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.furnitureType = furnitureType;
    }

//    public void SetFurniture(AddFurnitureReqDTO reqDTO){
//        this.name = reqDTO.getName();
//        this.sizeX = reqDTO.getSizeX();
//        this.sizeY = reqDTO.getSizeY();
//        this.furnitureType = reqDTO.getFurnitureType();
//    }
}
