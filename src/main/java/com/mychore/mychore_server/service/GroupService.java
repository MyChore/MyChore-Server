package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.Group.AddFurnitureResDTO;
import com.mychore.mychore_server.dto.Group.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.RoomInfoDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.repository.FurnitureRepository;
import com.mychore.mychore_server.repository.GroupRepository;
import com.mychore.mychore_server.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    public final FurnitureRepository furnitureRepository;
    public final GroupRepository groupRepository;
    public final RoomRepository roomRepository;

    public String addFurniture(AddFurnitureResDTO reqDTO){
        Furniture furniture = new Furniture(reqDTO);
        furnitureRepository.save(furniture);
        return furniture.getName();
    }

    public ResponseCustom<PostGroupResDTO> postGroup(PostGroupReqDTO reqDTO){
        String inviteCode = createInviteCode();
        Group groupReq = new Group(
                reqDTO.getFloorName(), inviteCode, reqDTO.getFloorType());
        groupRepository.save(groupReq);

        Group group = groupRepository.findByInviteCode(inviteCode);

        for(RoomInfoDTO roomInfoDTO : reqDTO.getRooms()){
            Room room = new Room(group, roomInfoDTO);
            roomRepository.save(room);
        }

        PostGroupResDTO resDTO = new PostGroupResDTO(group.getId(), inviteCode);
        return ResponseCustom.OK(resDTO);
    }

    private String createInviteCode(){
        return "abcdefgh";
    }
}
