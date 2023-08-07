package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.GroupAssembler;
import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.*;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Res.*;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.FurnitureType;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;
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
    private final GroupAssembler groupAssembler;

    public Furniture addFurniture(AddFurnitureReqDTO reqDTO){
        Furniture furniture = groupAssembler.toFurnitureEntity(reqDTO);
        return furnitureRepository.save(furniture);
    }

    public PostGroupResDTO postGroup(PostGroupReqDTO reqDTO, Long userId){
        String inviteCode = createInviteCode();
        Group group = groupRepository.save(
                groupAssembler.toGroupEntity(inviteCode, reqDTO.getFloorName(), reqDTO.getFloorType()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
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
            Room room = roomRepository.findById(roomInfo.getRoomId())
                    .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM));
            for(FurnitureInfoDTO furnInfo : roomInfo.getFurnitureInfoList()){
                Furniture furniture = furnitureRepository.findById(furnInfo.getFurnitureId())
                        .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_FURNITURE));
                roomFurnitureRepository.save(groupAssembler.toRoomFurnitureEntity(room, furniture, furnInfo));
            }
        }
        return resDTO;
    }

    private Group validationCheck(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        groupUserRepository.findByUserAndGroupAndStatus(user, group, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_PERMISSION));

        return group;
    }

    private List<UserInfoDTO> getUserInfoList(Group group){
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        List<GroupUser> memberList = groupUserRepository.findGroupUsersByGroupAndStatus(group, ACTIVE_STATUS);
        for(GroupUser member: memberList) { userInfoDTOList.add(groupAssembler.toUserInfoDto(member)); }

        return userInfoDTOList;
    }

    public StaticDataResDTO getStaticData(Long groupId, Long userId){
        Group group = validationCheck(groupId, userId);

        List<UserInfoDTO> userInfoDTOList = getUserInfoList(group);

        List<RoomInfoDTO> roomInfoDTOList = new ArrayList<>();
        List<Room> roomList = roomRepository.findRoomsByGroupAndStatus(group, ACTIVE_STATUS);
        for(Room room: roomList){
            List<RoomFurniture> furnitureList = roomFurnitureRepository.findAllByRoomAndStatus(room, ACTIVE_STATUS);
            List<PlacedFurnitureInfoDTO> furnitureInfoDTOList = new ArrayList<>();
            for(RoomFurniture furniture : furnitureList){
                furnitureInfoDTOList.add(groupAssembler.toPlacedFurnitureInfoDto(furniture));
            }
            roomInfoDTOList.add(groupAssembler.toRoomInfoDto(room, furnitureInfoDTOList));
        }

        return groupAssembler.toStaticDataResDto(group, userInfoDTOList, roomInfoDTOList);
    }

    private GroupListInfoDTO getGroupInfo(Long groupId, Long userId){
        Group group = validationCheck(groupId, userId);
        GroupListInfoDTO groupInfo = groupAssembler.toGroupListInfoDto(group);

        List<UserInfoDTO> userInfoDTOList = getUserInfoList(group);
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

    public List<RoomChoreResDTO> getRoomChoreInfo(Long groupId, Long roomId, Long userId){
        Group group = validationCheck(groupId, userId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM));
        List<RoomFurniture> furnitureList = roomFurnitureRepository.findAllByRoomAndStatus(room, ACTIVE_STATUS);

        List<RoomChoreResDTO> resDTO = new ArrayList<>();
        for(RoomFurniture furniture: furnitureList){
            List<Chore> choreList = choreRepository.findAllByRoomFurnitureAndStatus(furniture, ACTIVE_STATUS);
            for(Chore chore: choreList){
                resDTO.add(groupAssembler.toRoomChoreResDto(chore));
            }
        }

        return resDTO;
    }

    private Group groupOwnerCheck(Long groupId, Long userId){
//        validation check
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        groupUserRepository.findByUserAndGroupAndStatus(user, group, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_PERMISSION));

//        owner check
        groupUserRepository.findByUserAndRoleAndStatus(user, OWNER, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_PERMISSION));

        return group;
    }
    public StaticDataResDTO updateGroupName(Long groupId, String newName, Long userId){
        Group group = groupOwnerCheck(groupId, userId);
        group.SetName(newName);
        return getStaticData(groupId, userId);
    }
}
