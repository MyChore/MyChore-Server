package com.mychore.mychore_server.dto.chore;

import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.chore.ChoreBadRequestException;
import com.mychore.mychore_server.exception.chore.ChoreNotFoundException;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChoreAssembler {

    private final ChoreRepository choreRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoomRepository roomRepository;
    private final RoomFurnitureRepository roomFurnitureRepository;

    public Chore toEntity(User user, RoomFurniture roomFurniture, Group group, ChoreCreateReq choreCreateReq) {

        return Chore.builder()
                .user(user)
                .roomFurniture(roomFurniture)
                .group(group)
                .name(choreCreateReq.getName())
                .isAcceptNoti(choreCreateReq.getIsAcceptNoti()==null ? true : choreCreateReq.getIsAcceptNoti())
                .startDate(choreCreateReq.getStartDate())
                .lastDate(choreCreateReq.getRepetition()==null? choreCreateReq.getStartDate() : choreCreateReq.getLastDate())
                .repetition(choreCreateReq.getRepetition())
                .notiTime(choreCreateReq.getIsAcceptNoti()==true ? choreCreateReq.getNotiTime() : null)
                .build();
    }

    // 엔티티 검증 로직
    public User findUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    public Room findRoomEntity(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 방을 찾을 수 없습니다."));
    }

    public RoomFurniture findRoomFurnitureEntity(Long roomFurnitureId) {
        return roomFurnitureRepository.findById(roomFurnitureId)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 가구를 찾을 수 없습니다."));
    }

    public Group findGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 그룹을 찾을 수 없습니다."));
    }

    public Chore findChoreEntity(Long choreId) {
        return choreRepository.findById(choreId)
                .orElseThrow(() -> new ChoreNotFoundException(choreId));
    }

    // findChores 검증 로직
    public Void isValidfindChoresParameter(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime) {

        String result = "";

        if (userId!=null) {
            findUserEntity(userId);
        }

        if (groupId!=null) {
            findGroupEntity(groupId);
        }

        if (roomId!=null) {
            findRoomEntity(roomId);
        }

        if (groupId==null) {
            result += (result=="") ? "그룹" : ", 그룹";
        }

        if (fromTime==null) {
            result += (result=="") ? "조회시작날짜" : ", 조회시작날짜";
        }

        if (toTime==null) {
            result += (result=="") ? "조회종료날짜" : ", 조회종료날짜";
        }

        if (fromTime!=null && toTime!=null) {
            if (fromTime.isAfter(toTime)) {
                result += (result=="") ? "조회구간" : ", 조회구간";
            }
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;

    }

    // updateChore 검증
    public Void isValidUpdateReqBody(Chore chore, ChoreUpdateReq choreUpdateReqDto) {
        String result = "";

        if (choreUpdateReqDto.getIsAcceptNoti()!=null) {
            if (choreUpdateReqDto.getIsAcceptNoti()==true && choreUpdateReqDto.getNotiTime()==null) {
                result += (result=="") ? "알림시간" : ", 알림시간";
            }
        }

        if (choreUpdateReqDto.getStartDate()!=null) {
            if(choreUpdateReqDto.getStartDate().isAfter(chore.getLastDate())) {
                result += (result=="") ? "시작날짜" : ", 시작날짜";
            }
        }

        if (choreUpdateReqDto.getLastDate()!=null) {
            if(choreUpdateReqDto.getLastDate().isBefore(chore.getStartDate())) {
                result += (result=="") ? "종료날짜" : ", 종료날짜";
            }
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;
    }


    //  setChoreLog 파라미터 확인하는 함수
    public Void isValidSetLogReqParameter(Chore chore, LocalDate setDate, Boolean bool) {
        String result = "";

        if (setDate == null ) {
            result += (result=="") ? "설정시간" : ", 설정시간";
        }

        if (setDate != null) {
            if (setDate.isBefore(chore.getStartDate()) ||
                    setDate.isAfter(chore.getLastDate())) {
                result += (result=="") ? "설정시간" : ", 설정시간";
            }
        }

        if (bool.equals(null)) {
            result += (result=="") ? "완료상태" : ", 완료상태";
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;
    }

    // saveChore Req 확인 함수
    public Void isValidSaveReqBody(ChoreCreateReq choreSaveReqDto) {

        String result = "";

        if (choreSaveReqDto.getName()=="" || choreSaveReqDto.getName()==null) {
            result += (result=="") ? "이름" : ", 이름";
        }

        if (choreSaveReqDto.getUserId()==null) {
            result += (result=="") ? "담당자" : ", 담당자";
        }

        if (choreSaveReqDto.getRoomFurnitureId()==null) {
            result += (result=="") ? "가구" : ", 가구";
        }

        if (choreSaveReqDto.getGroupId()==null) {
            result += (result=="") ? "그룹" : ", 그룹";
        }

        if (choreSaveReqDto.getStartDate()==null) {
            result += (result=="") ? "시작날짜" : ", 시작날짜";
        }

        if (choreSaveReqDto.getStartDate()!=null && choreSaveReqDto.getLastDate()!=null) {
            if (choreSaveReqDto.getStartDate().isAfter(choreSaveReqDto.getLastDate())) {
                result += (result=="") ? "종료날짜" : ", 종료날짜";
            }
        }

        if (choreSaveReqDto.getIsAcceptNoti()!=null&&choreSaveReqDto.getIsAcceptNoti()==true) {
            if (choreSaveReqDto.getNotiTime()==null) {
                result += (result=="") ? "알림시간" : ", 알림시간";
            }
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;
    }



}
