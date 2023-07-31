package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.dto.Group.AddFurnitureResDTO;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
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

    @Builder
    public Furniture(AddFurnitureResDTO reqDTO){
        this.name = reqDTO.getName();
        this.sizeX = reqDTO.getSizeX();
        this.sizeY = reqDTO.getSizeY();
    }
}
