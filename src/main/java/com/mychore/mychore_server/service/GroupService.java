package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.GroupAssembler;
import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.*;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Req.UpdateRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Res.*;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.FurnitureType;
import com.mychore.mychore_server.global.constants.Role;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;
import static com.mychore.mychore_server.global.constants.Constant.INACTIVE_STATUS;
import static com.mychore.mychore_server.global.constants.Role.MEMBER;
import static com.mychore.mychore_server.global.constants.Role.OWNER;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final FurnitureRepository furnitureRepository;
    private final GroupRepository groupRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final GroupUserRepository groupUserRepository;
    private final RoomFurnitureRepository roomFurnitureRepository;
    private final ChoreRepository choreRepository;
    private final ChoreLogRepository choreLogRepository;

    private final GroupAssembler groupAssembler;

    public Furniture addFurniture(AddFurnitureReqDTO reqDTO){
        Furniture furniture = groupAssembler.toFurnitureEntity(reqDTO);
        return furnitureRepository.save(furniture);
    }

    public PostGroupResDTO postGroup(PostGroupReqDTO reqDTO, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));

        String inviteCode = createInviteCode();
        Group group = groupRepository.save(
                groupAssembler.toGroupEntity(inviteCode, reqDTO.getFloorName(), reqDTO.getFloorType()));

        GroupUser groupUser = groupUserRepository.save(
                groupAssembler.toGroupUserEntity(group, user, OWNER));

        PostGroupResDTO resDTO = groupAssembler.toPostGroupResDto(group.getId(), inviteCode, groupUser.getId());

        List<Long> roomIdList = new ArrayList<>();
        for(RoomInfoDTO roomInfoDTO : reqDTO.getRooms()){
            Room room = groupAssembler.toRoomEntity(group, roomInfoDTO);
            roomIdList.add(roomRepository.save(room).getId());
        }

        resDTO.SetRoomIdList(roomIdList);
        return resDTO;
    }

    private String createInviteCode(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 8;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Long joinGroup(String inviteCode, Long userId){
        Group group = groupRepository.findByInviteCodeAndStatus(inviteCode, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_INVITATION_CODE));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));

        groupUserRepository.findByUserAndGroupAndStatus(user, group, ACTIVE_STATUS)
                .ifPresent( m -> { throw new BaseException(BaseResponseCode.ALREADY_JOIN_GROUP); });

        groupUserRepository.save(groupAssembler.toGroupUserEntity(group, user, MEMBER));
        return group.getId();
    }

    public List<FurnitureResDTO> getFurnitureList(String furnitureName){
        List<FurnitureResDTO> resDTO = new ArrayList<>();

        FurnitureType furnitureType = FurnitureType.getByName(furnitureName);
        for(Furniture furniture : furnitureRepository.findByFurnitureTypeAndStatus(furnitureType, ACTIVE_STATUS)){
            resDTO.add(groupAssembler.toFurnitureResDto(furniture));
        }
        return resDTO;
    }

    public PostRoomResDTO postRoomDetail(PostRoomReqDTO reqDTO, Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        PostRoomResDTO resDTO = groupAssembler.toPostRoomResDto(group);

        for(RoomFurnitureInfoDTO roomInfo : reqDTO.getRoomFurnitureInfoList()){
            Room room = roomRepository.findRoomByIdAndGroupAndStatus(roomInfo.getRoomId(), group, ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM));
            for(FurnitureInfoDTO furnInfo : roomInfo.getFurnitureInfoList()){
                Furniture furniture = furnitureRepository.findById(furnInfo.getFurnitureId())
                        .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_FURNITURE));
                roomFurnitureRepository.save(groupAssembler.toRoomFurnitureEntity(room, furniture, furnInfo));
            }
        }
        return resDTO;
    }

    private CheckResDTO validationCheck(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        GroupUser groupUser = groupUserRepository.findByUserAndGroupAndStatus(user, group, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_MEMBER));

        return groupAssembler.toCheckResDto(group, user, groupUser);
    }

    private List<UserInfoDTO> getUserInfoList(Group group){
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        List<GroupUser> memberList = groupUserRepository.findGroupUsersByGroupAndStatus(group, ACTIVE_STATUS);
        for(GroupUser member: memberList) { userInfoDTOList.add(groupAssembler.toUserInfoDto(member)); }

        return userInfoDTOList;
    }

    public StaticDataResDTO getStaticData(Long groupId, Long userId){
        CheckResDTO check = validationCheck(groupId, userId);

        List<UserInfoDTO> userInfoDTOList = getUserInfoList(check.getGroup());

        List<GetRoomInfoDTO> roomInfoDTOList = new ArrayList<>();
        List<Room> roomList = roomRepository.findRoomsByGroupAndStatus(check.getGroup(), ACTIVE_STATUS);
        for(Room room: roomList){
            List<RoomFurniture> furnitureList = roomFurnitureRepository.findAllByRoomAndStatus(room, ACTIVE_STATUS);
            List<PlacedFurnitureInfoDTO> furnitureInfoDTOList = new ArrayList<>();
            for(RoomFurniture furniture : furnitureList){
                furnitureInfoDTOList.add(groupAssembler.toPlacedFurnitureInfoDto(furniture));
            }
            roomInfoDTOList.add(groupAssembler.toGetRoomInfoDto(room, furnitureInfoDTOList));
        }

        return groupAssembler.toStaticDataResDto(check.getGroup(), userInfoDTOList, roomInfoDTOList);
    }

    private GroupListInfoDTO getGroupInfo(Long groupId, Long userId){
        CheckResDTO check = validationCheck(groupId, userId);
        GroupListInfoDTO groupInfo = groupAssembler.toGroupListInfoDto(check.getGroup());

        List<UserInfoDTO> userInfoDTOList = getUserInfoList(check.getGroup());
        groupInfo.SetMemberList(userInfoDTOList);

        return groupInfo;
    }

    public List<GroupListInfoDTO> getGroupInfoList(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        List<GroupUser> groupUserList = groupUserRepository.findByUserAndStatus(user, ACTIVE_STATUS);
        List<GroupListInfoDTO> groupListInfoDTOList = new ArrayList<>();

        for(GroupUser groupUser: groupUserList){
            GroupListInfoDTO groupInfo = getGroupInfo(groupUser.getGroup().getId(), userId);
            groupInfo.SetRole(groupUser.getRole());
            groupListInfoDTOList.add(groupInfo);
        }
        return groupListInfoDTOList;
    }


    public List<RoomChoreResDTO> getRoomChoreInfo(Long groupId, Long roomId, LocalDate date, Long userId){
        validationCheck(groupId, userId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM));
        List<RoomFurniture> furnitureList = roomFurnitureRepository.findAllByRoomAndStatus(room, ACTIVE_STATUS);

        List<ChoreLog> todayChoreList = choreLogRepository.findAllBySetDateAndStatus(date, ACTIVE_STATUS);
        List<RoomChoreResDTO> resDTO = new ArrayList<>();
        for(RoomFurniture furniture: furnitureList){
            List<Chore> choreList = choreRepository.findAllByRoomFurnitureAndStatus(furniture, ACTIVE_STATUS);
            for(ChoreLog todayChore: todayChoreList) {
                for (Chore chore : choreList) {
                    if (todayChore.getChore() == chore) {
                        RoomChoreResDTO roomChoreResDTO = groupAssembler.toRoomChoreResDto(chore);
                        roomChoreResDTO.setIsComplete(todayChore.getIsComplete());
                        resDTO.add(roomChoreResDTO);
                    }
                }
            }
        }
        return resDTO;
    }

    private CheckResDTO ownerCheck(Long groupId, Long userId){
//        validation check
        CheckResDTO check = validationCheck(groupId, userId);

//        role check
        groupUserRepository.findByUserAndGroupAndRoleAndStatus(check.getUser(), check.getGroup(), OWNER, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_OWNER_PERMISSION));

        return check;
    }

    public StaticDataResDTO updateGroupName(Long groupId, String newName, Long userId){
        CheckResDTO check = ownerCheck(groupId, userId);
        check.getGroup().SetName(newName);
        return getStaticData(groupId, userId);
    }

    public StaticDataResDTO updateGroupFurniture(Long groupId, UpdateRoomReqDTO reqDTO, Long userId){
        CheckResDTO check = ownerCheck(groupId, userId);

        for(UpdateRoomFurnitureInfoDTO roomFurnitureInfo: reqDTO.getRoomFurnitureInfoList()){
            Room room = roomRepository.findRoomByIdAndGroupAndStatus(roomFurnitureInfo.getRoomId(), check.getGroup(), ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM));
            for(UpdateFurnitureInfoDTO furnitureInfo: roomFurnitureInfo.getFurnitureInfoList()){
                Furniture furniture = furnitureRepository.findByIdAndStatus(furnitureInfo.getFurnitureId(), ACTIVE_STATUS)
                        .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_FURNITURE));
                if(furnitureInfo.getRoomFurnId() == null){
                    roomFurnitureRepository.save(groupAssembler.toRoomFurnitureEntity(room, furniture, furnitureInfo));
                }
                else{
                    RoomFurniture roomFurniture = roomFurnitureRepository.findRoomFurnitureByIdAndStatus(furnitureInfo.getRoomFurnId(), ACTIVE_STATUS)
                            .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM_FURNITURE));
                    roomFurniture.SetNewInfo(furnitureInfo);
                    roomFurnitureRepository.save(roomFurniture);
                }
            }
        }
        return getStaticData(groupId, userId);
    }

    public List<RemainChoreResDTO> getRemainChore(Long groupId, LocalDate date, Long userId){
        CheckResDTO check = validationCheck(groupId, userId);
        List<Room> roomList = roomRepository.findRoomsByGroupAndStatus(check.getGroup(), ACTIVE_STATUS);
        List<ChoreLog> todayChoreList = choreLogRepository.findAllBySetDateAndIsCompleteAndStatus(date, false, ACTIVE_STATUS);

        List<RemainChoreResDTO> resDTO = new ArrayList<>();
        for(Room room: roomList){
            List<RoomFurniture> furnitureList = roomFurnitureRepository.findAllByRoomAndStatus(room, ACTIVE_STATUS);
            int count = 0;
            for(RoomFurniture furniture: furnitureList){
                for(ChoreLog choreLog: todayChoreList){
                    if(choreLog.getChore().getRoomFurniture()==furniture){
                        count+=1;
                    }
                }
            }
            RemainChoreResDTO remainChoreResDTO = groupAssembler.toRemainChoreResDto(room, count);
            resDTO.add(remainChoreResDTO);
        }
        return resDTO;
    }

    public String withdrawUser(Long groupId, Long memberId, Long ownerId){
        CheckResDTO ownerCheck = ownerCheck(groupId, ownerId);
        CheckResDTO memberCheck = validationCheck(groupId, memberId);
        groupUserRepository.findByUserAndGroupAndRoleAndStatus
                        (memberCheck.getUser(), memberCheck.getGroup(), MEMBER, ACTIVE_STATUS)
                        .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_DELETE_GROUP)); //그룹장의 추방을 요청한 경우

        setInactive(memberCheck);
        return "성공적으로 추방 되었습니다.";
    }

    public String deleteUser(Long groupId, Long userId){
        CheckResDTO memberCheck = validationCheck(groupId, userId);
        groupUserRepository.findByUserAndGroupAndRoleAndStatus
                        (memberCheck.getUser(), memberCheck.getGroup(), MEMBER, ACTIVE_STATUS)
                        .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_DELETE_GROUP)); //그룹장의 추방을 요청한 경우
        setInactive(memberCheck);
        return "성공적으로 탈퇴 되었습니다.";
    }

    private void setInactive(CheckResDTO checkResDTO){
        GroupUser groupUser = checkResDTO.getGroupUser();
        List<Chore> choreList = choreRepository.findAllByGroupAndUserAndStatus(checkResDTO.getGroup(), checkResDTO.getUser(), ACTIVE_STATUS);
        for(Chore chore: choreList){
            List<ChoreLog> choreLogList = choreLogRepository.findChoreLogsByChore(chore);
            for(ChoreLog choreLog: choreLogList){
                choreLog.setStatus(INACTIVE_STATUS);
                choreLogRepository.save(choreLog);
            }
            chore.setStatus(INACTIVE_STATUS);
            choreRepository.save(chore);
        }
        groupUser.setStatus(INACTIVE_STATUS);
        groupUserRepository.save(groupUser);
    }

//    public List<Object> testJoin(LocalDate date){
//
//        List<Object> choreLogList = choreLogRepository.getChoreLogWithChore(date);
////        for(Object choreLog : choreLogList){
////            choreLog.
////        }
//        return choreLogList;
//    }
}
