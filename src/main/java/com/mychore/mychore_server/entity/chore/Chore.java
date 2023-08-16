package com.mychore.mychore_server.entity.chore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.global.constants.Repetition;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.entityListener.ChoreEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Getter
@SQLDelete(sql = "UPDATE chore SET status = 'inactive', updated_at = current_timestamp WHERE chore_id = ?")
@EntityListeners(ChoreEntityListener.class)
public class Chore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chore_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_furn_id")
    @JsonIgnore
    private RoomFurniture roomFurniture;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

    @NonNull
    @Column(length = 20)
    private String name;

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAcceptNoti = true;

    @NonNull
    private LocalDate startDate;
    private LocalDate lastDate;
    private LocalTime notiTime;

    @Enumerated(EnumType.STRING)
    private Repetition repetition;

    @OneToMany(mappedBy = "chore")
    @Filter(name = "choreLogDateFilter", condition = "setDate BETWEEN :fromTime AND :toTime")
    private List<ChoreLog> choreLogList = new ArrayList<>();

    public void updateInfo(ChoreUpdateReq choreUpdateReqDto) {
        this.name = choreUpdateReqDto.getName();
        this.isAcceptNoti = choreUpdateReqDto.getIsAcceptNoti();
        this.startDate = choreUpdateReqDto.getStartDate();
        this.lastDate = choreUpdateReqDto.getLastDate();
        this.repetition = choreUpdateReqDto.getRepetition();
        this.notiTime = choreUpdateReqDto.getNotiTime();
    }

    public void updateUser(User newUser) {
        this.user = newUser;
    }

    public void updateRoomFurniture(RoomFurniture roomFurniture) {
        this.roomFurniture = roomFurniture;
    }


}
