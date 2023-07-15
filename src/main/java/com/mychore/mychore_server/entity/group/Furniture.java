package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Furniture extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "furniture_id")
    private Long id;

    @NonNull
    private int sizeX;

    @NonNull
    private int sizeY;

    @NonNull
    @Column(length = 10)
    private String name;

    private String imgKey;
}
