package com.mychore.mychore_server.global.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RoomType {
    BATHROOM("화장실"),
    LIVING_ROOM("거실"),
    ROOM("방");

    private final String roomTypeName;

    RoomType (String roomTypeName) { this.roomTypeName = roomTypeName; }

    public static RoomType getByName(String name){
        return Arrays.stream(RoomType.values())
                .filter(roomType -> roomType.getRoomTypeName().equals(name))
                .findAny().orElse(null);
    }
}
