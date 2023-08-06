package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.GroupAssembler;
import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.*;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Res.FurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostRoomResDTO;
import com.mychore.mychore_server.dto.Group.Res.StaticDataResDTO;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.group.*;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.global.constants.FurnitureType;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final GroupAssembler groupAssembler;

    public Furniture addFurniture(AddFurnitureReqDTO reqDTO){
        Furniture furniture = groupAssembler.toFurnitureEntity(reqDTO);
        return furnitureRepository.save(furniture);
    }

    public PostGroupResDTO postGroup(PostGroupReqDTO reqDTO, Long userId){
        String inviteCode = createInviteCode();
        Group group = groupRepository.save(
                groupAssembler.toGroupEntity(inviteCode, reqDTO.getFloorName(), reqDTO.getFloorType()));

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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
        Group group = groupRepository.findByInviteCodeAndStatus(inviteCode, ACTIVE_STATUS).orElseThrow(InvalidInvitationCodeException::new);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        groupUserRepository.findByUserAndGroupAndStatus(user, group, ACTIVE_STATUS).ifPresent( m -> {throw new GroupAlreadyExistException();});

        groupUserRepository.save(groupAssembler.toGroupUserEntity(group, user, MEMBER));
        return group.getId();
    }

    public List<FurnitureResDTO> getFurnitureList(String furnitureName){
        List<FurnitureResDTO> resDTO = new ArrayList<>();

        FurnitureType furnitureType = FurnitureType.getByName(furnitureName);
        if(furnitureType==null){ throw new InvalidTypeNameException(); }
        for(Furniture furniture : furnitureRepository.findByFurnitureTypeAndStatus(furnitureType, ACTIVE_STATUS)){
            resDTO.add(groupAssembler.toFurnitureResDto(furniture));
        }
        return resDTO;
    }

    public PostRoomResDTO postRoomDetail(PostRoomReqDTO reqDTO, Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        PostRoomResDTO resDTO = groupAssembler.toPostRoomResDto(group);

        for(RoomFurnitureInfoDTO roomInfo : reqDTO.getRoomFurnitureInfoList()){
            Room room = roomRepository.findById(roomInfo.getRoomId()).orElseThrow(RoomNotFoundException::new);
            for(FurnitureInfoDTO furnInfo : roomInfo.getFurnitureInfoList()){
                Furniture furniture = furnitureRepository.findById(furnInfo.getFurnitureId()).orElseThrow(FurnitureNotFoundException::new);
                roomFurnitureRepository.save(groupAssembler.toRoomFurnitureEntity(room, furniture, furnInfo));
            }
        }
        return resDTO;
    }

    public StaticDataResDTO getStaticData(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        groupUserRepository.findByUserAndGroupAndStatus(user, group, ACTIVE_STATUS).orElseThrow(InvalidApproachException::new);

        List<RoomInfoDTO> roomInfoDTOList = new ArrayList<>();
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();

        List<GroupUser> memberList = groupUserRepository.findGroupUsersByGroupAndStatus(group, ACTIVE_STATUS);
        for(GroupUser member: memberList) { userInfoDTOList.add(new UserInfoDTO(member)); }
        List<Room> roomList = roomRepository.findRoomsByGroupAndStatus(group, ACTIVE_STATUS);
        for(Room room: roomList){
            List<RoomFurniture> furnitureList = roomFurnitureRepository.findAllByRoomAndStatus(room, ACTIVE_STATUS);
            List<PlacedFurnitureInfoDTO> furnitureInfoDTOList = new ArrayList<>();
            for(RoomFurniture furniture : furnitureList){
                furnitureInfoDTOList.add(new PlacedFurnitureInfoDTO(furniture));
            }
            roomInfoDTOList.add(new RoomInfoDTO(room, furnitureInfoDTOList));
        }


        return new StaticDataResDTO(group, userInfoDTOList, roomInfoDTOList);
    }
}
