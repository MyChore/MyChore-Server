package com.mychore.mychore_server.global.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FloorType {
    ONE_ROOM(1, "원룸"),
    DIV_ONE_ROOM(1.5, "분리형 원룸"),
    TWO_ROOM(2, "투룸"),
    THREE_ROOM(3, "쓰리룸");

    private final double typeNum;
    private final String typeName;

    FloorType(double typeNum, String typeName) {
        this.typeNum = typeNum;
        this.typeName = typeName;
    }

    public static FloorType getFloorTypeByName(String name){
        return Arrays.stream(FloorType.values())
                .filter(floorType -> floorType.getTypeName().equals(name))
                .findAny().orElse(null);
    }

    public static FloorType getFloorTypeByNum(Double num){
        return Arrays.stream(FloorType.values())
                .filter(floorType -> floorType.getTypeNum() == num)
                .findAny().orElse(null);
    }
}
