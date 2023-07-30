package com.mychore.mychore_server.dto;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Repetition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class ChoreDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response implements Comparable<Response>{

        private Long id;

        // 다른 DTO 개발시 DTO로 변경 예정
        private Long userId;
        private Long roomFurnitureId;
        private Long groupId;
        private Long roomId;

        private String name;
        private Boolean isAcceptNoti;
        private LocalDate startDate;
        private LocalDate lastDate;
        private Repetition repetition;

        private LocalDate setDate;
        private Boolean completeStatus;
        private LocalTime notiTime;

        public Response(Chore chore) {
            this.id = chore.getId();
            this.userId = chore.getUser().getId();
            this.roomFurnitureId = chore.getRoomFurniture().getId();
            this.groupId = chore.getGroup().getId();
            this.roomId = chore.getRoomFurniture().getRoom().getId();
            this.name = chore.getName();
            this.isAcceptNoti = chore.getIsAcceptNoti();
            this.startDate = chore.getStartDate();
            this.lastDate = chore.getLastDate();
            this.repetition = chore.getRepetition();
            this.notiTime = chore.getNotiTime();
        }


        @Override
        public int compareTo(Response other) {
            return this.setDate.compareTo(other.getSetDate());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class EntityResponse{

        private Long id;

        // 다른 DTO 개발시 DTO로 변경 예정
        private Long userId;
        private Long roomFurnitureId;
        private Long groupId;
        private Long roomId;

        private String name;
        private Boolean isAcceptNoti;
        private LocalDate startDate;
        private LocalDate lastDate;
        private Repetition repetition;
        private LocalTime notiTime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Create {

        private Long userId;
        private Long roomFurnitureId;
        private Long groupId;

        private String name;
        private Boolean isAcceptNoti;
        private LocalDate startDate;
        private LocalDate lastDate;
        private Repetition repetition;
        private LocalTime notiTime;

        public Chore toEntity(User user, RoomFurniture roomFurniture, Group group) {

            return Chore.builder()
                    .user(user)
                    .roomFurniture(roomFurniture)
                    .group(group)
                    .name(this.name)
                    .isAcceptNoti(this.isAcceptNoti==null ? true : this.isAcceptNoti)
                    .startDate(this.startDate)
                    .lastDate(this.repetition==null? this.startDate : this.lastDate)
                    .repetition(this.repetition)
                    .notiTime(this.isAcceptNoti==true ? this.notiTime : null)
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Update {

        private Long userId;
        private Long roomFurnitureId;

        private String name;
        private Boolean isAcceptNoti;
        private LocalDate startDate;
        private LocalDate lastDate;
        private Repetition repetition;
        private LocalTime notiTime;
    }

}
