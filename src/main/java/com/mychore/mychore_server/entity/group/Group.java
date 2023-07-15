package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.global.constants.FloorType;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Group extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    @NonNull
    @Column(length = 10)
    private String inviteCode;

    @NonNull
    @Column(length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @NonNull
    private FloorType floorType;
}
