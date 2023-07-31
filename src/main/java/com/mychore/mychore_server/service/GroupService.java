package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.Req.InfoList.FurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Res.AddFurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Res.FurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostRoomResDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomFurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.mychore.mychore_server.global.constants.Role.MEMBER;
import static com.mychore.mychore_server.global.constants.Role.OWNER;

@Service
@RequiredArgsConstructor
public class GroupService {
    public final FurnitureRepository furnitureRepository;
    public final GroupRepository groupRepository;
    public final RoomRepository roomRepository;
    public final UserRepository userRepository;
    public final GroupUserRepository groupUserRepository;
    public final RoomFurnitureRepository roomFurnitureRepository;

    public ResponseCustom<Furniture> addFurniture(AddFurnitureResDTO reqDTO){
        Furniture furniture = new Furniture(reqDTO);
        Furniture res = furnitureRepository.save(furniture);
        return ResponseCustom.OK(res);
    }

    public ResponseCustom<PostGroupResDTO> postGroup(PostGroupReqDTO reqDTO, Long userId){
        String inviteCode = createInviteCode();
        Group group = new Group(
                inviteCode, reqDTO.getFloorName(), reqDTO.getFloorType());
        Long groupId = groupRepository.save(group).getId();
        group.setId(groupId);

        Optional<User> user = userRepository.findById(userId);
        GroupUser groupUser = new GroupUser(group, user.get(), OWNER);
        groupUserRepository.save(groupUser);

        PostGroupResDTO resDTO = new PostGroupResDTO(group.getId(), inviteCode, groupUser.getId());

        List<Long> roomIdList = new ArrayList<>();
        for(RoomInfoDTO roomInfoDTO : reqDTO.getRooms()){
            Room room = new Room(group, roomInfoDTO);
            roomIdList.add(roomRepository.save(room).getId());
        }

        resDTO.setRoomIdList(roomIdList);
        return ResponseCustom.OK(resDTO);
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

    public ResponseCustom<Long> joinGroup(String inviteCode, Long userId){
        Optional<Group> group = groupRepository.findByInviteCode(inviteCode);
        if(group.isPresent()){
            Optional<User> user = userRepository.findById(userId);

            Optional<GroupUser> groupUser_check = groupUserRepository.findByUserAndGroup(user.get(), group.get());
            if(groupUser_check.isPresent()){
                return ResponseCustom.BAD_REQUEST("이미 그룹에 참여하고 있습니다.");
            }
            GroupUser groupUser = new GroupUser(group.get(), user.get(), MEMBER);
            groupUserRepository.save(groupUser);

            return ResponseCustom.OK(group.get().getId());
        }
        return ResponseCustom.BAD_REQUEST("유효하지 않은 초대코드 입니다.");
    }

    public ResponseCustom<List<FurnitureResDTO>> getFurnitureList(){
        List<FurnitureResDTO> resDTO = new ArrayList<>();
        for(Furniture furniture : furnitureRepository.findAll()){
            resDTO.add(new FurnitureResDTO(furniture));
        }
        return ResponseCustom.OK(resDTO);
    }

    public ResponseCustom<PostRoomResDTO> postRoomDetail(PostRoomReqDTO reqDTO){
        Group group = groupRepository.findById(reqDTO.getGroupId()).get();
        PostRoomResDTO resDTO = new PostRoomResDTO(group.getName(), group.getInviteCode());

        for(RoomFurnitureInfoDTO roomInfo : reqDTO.getRoomFurnitureInfoList()){
            Room room = roomRepository.findById(roomInfo.getRoomId()).get();
            for(FurnitureInfoDTO furnInfo : roomInfo.getFurnitureInfoList()){
                Furniture furniture = furnitureRepository.findById(furnInfo.getFurnitureId()).get();
                roomFurnitureRepository.save(new RoomFurniture(room, furniture, furnInfo));
            }
        }
        return ResponseCustom.OK(resDTO);
    }
}
