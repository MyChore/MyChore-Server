package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.GroupAssembler;
import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.FurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Res.FurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostRoomResDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomFurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.group.*;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.global.constants.FurnitureType;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Furniture furniture = groupAssembler.toEntity(reqDTO);
        return furnitureRepository.save(furniture);
    }

    public PostGroupResDTO postGroup(PostGroupReqDTO reqDTO, Long userId){
        String inviteCode = createInviteCode();
        Group group = groupRepository.save(
                groupAssembler.toEntity(inviteCode, reqDTO.getFloorName(), reqDTO.getFloorType()));

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GroupUser groupUser = groupUserRepository.save(
                groupAssembler.toEntity(group, user, OWNER));

        PostGroupResDTO resDTO = new PostGroupResDTO(group.getId(), inviteCode, groupUser.getId());

        List<Long> roomIdList = new ArrayList<>();
        for(RoomInfoDTO roomInfoDTO : reqDTO.getRooms()){
            Room room = groupAssembler.toEntity(group, roomInfoDTO);
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

        groupUserRepository.save(groupAssembler.toEntity(group, user, MEMBER));
        return group.getId();
    }

    public List<FurnitureResDTO> getFurnitureList(String furnitureName){
        List<FurnitureResDTO> resDTO = new ArrayList<>();

        FurnitureType furnitureType = FurnitureType.getByName(furnitureName);
        if(furnitureType==null){ throw new InvalidTypeNameException(); }
        for(Furniture furniture : furnitureRepository.findByFurnitureType(furnitureType)){
            resDTO.add(new FurnitureResDTO(furniture));
        }
        return resDTO;
    }

    public PostRoomResDTO postRoomDetail(PostRoomReqDTO reqDTO, Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        PostRoomResDTO resDTO = new PostRoomResDTO(group.getName(), group.getInviteCode());

        for(RoomFurnitureInfoDTO roomInfo : reqDTO.getRoomFurnitureInfoList()){
            Room room = roomRepository.findById(roomInfo.getRoomId()).orElseThrow(RoomNotFoundException::new);
            for(FurnitureInfoDTO furnInfo : roomInfo.getFurnitureInfoList()){
                Furniture furniture = furnitureRepository.findById(furnInfo.getFurnitureId()).orElseThrow(FurnitureNotFoundException::new);
                roomFurnitureRepository.save(groupAssembler.toEntity(room, furniture, furnInfo));
            }
        }
        return resDTO;
    }
}
