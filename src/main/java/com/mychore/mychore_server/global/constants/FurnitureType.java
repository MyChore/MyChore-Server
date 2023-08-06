package com.mychore.mychore_server.global.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FurnitureType {
    RELAX("휴식"), STORAGE("책상,수납"), KITCHEN("부엌"), BATHROOM("욕실"), ETC("기타");

    private final String furnitureName;

    FurnitureType(String furnitureName) { this.furnitureName = furnitureName; }

    public static FurnitureType getByName(String name){
        return Arrays.stream(FurnitureType.values())
                .filter(furnitureType -> furnitureType.getFurnitureName().equals(name))
                .findAny().orElse(null);
    }
}
