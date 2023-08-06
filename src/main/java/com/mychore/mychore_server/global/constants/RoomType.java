package com.mychore.mychore_server.global.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RoomType {
    BATHROOM("화장실"),
    LIVING_ROOM("거실"),
    ROOM("방");

    private final String roomName;

    RoomType (String roomName) { this.roomName = roomName; }

    public static RoomType getByName(String name){
        return Arrays.stream(RoomType.values())
                .filter(roomType -> roomType.getRoomName().equals(name))
                .findAny().orElse(null);
    }
}
