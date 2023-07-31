package com.mychore.mychore_server.entity.chore;

import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.global.constants.Repetition;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
public class Chore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chore_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_furn_id")
    private RoomFurniture roomFurniture;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
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
    private List<ChoreLog> choreLogList = new ArrayList<>();

    public void updateInfo(ChoreUpdateReq choreUpdateReqDto) {
        if (choreUpdateReqDto.getName() != null) this.name = choreUpdateReqDto.getName();
        if (choreUpdateReqDto.getIsAcceptNoti() != null) this.isAcceptNoti = choreUpdateReqDto.getIsAcceptNoti();
        if (choreUpdateReqDto.getStartDate() != null) this.startDate = choreUpdateReqDto.getStartDate();
        if (choreUpdateReqDto.getLastDate() != null) this.lastDate = choreUpdateReqDto.getLastDate();
        if (choreUpdateReqDto.getRepetition() != null) this.repetition = choreUpdateReqDto.getRepetition();
        if (choreUpdateReqDto.getNotiTime() != null) this.notiTime = choreUpdateReqDto.getNotiTime();
        if (choreUpdateReqDto.getIsAcceptNoti()!=null && !choreUpdateReqDto.getIsAcceptNoti()) this.notiTime = null;
    }

    public void updateUser(User newUser) {
        this.user = newUser;
    }

    public void updateRoomFurniture(RoomFurniture roomFurniture) {
        this.roomFurniture = roomFurniture;
    }


}
