package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.AddFurnitureResDTO;
import com.mychore.mychore_server.dto.Group.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.RoomInfoDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.mychore.mychore_server.global.constants.Role.OWNER;

@Service
@RequiredArgsConstructor
public class GroupService {
    public final FurnitureRepository furnitureRepository;
    public final GroupRepository groupRepository;
    public final RoomRepository roomRepository;
    public final UserRepository userRepository;
    public final GroupUserRepository groupUserRepository;

    public String addFurniture(AddFurnitureResDTO reqDTO){
        Furniture furniture = new Furniture(reqDTO);
        furnitureRepository.save(furniture);
        return furniture.getName();
    }

    public ResponseCustom<PostGroupResDTO> postGroup(PostGroupReqDTO reqDTO, Long userId){
        String inviteCode = createInviteCode();
        Group group = new Group(
                reqDTO.getFloorName(), inviteCode, reqDTO.getFloorType());
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
}
