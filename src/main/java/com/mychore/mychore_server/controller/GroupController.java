package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Res.FurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostRoomResDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.global.constants.FurnitureType;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {
    public final GroupService groupService;
    @PostMapping("/add")
    public ResponseCustom<Furniture> addFurniture(@Valid @RequestBody AddFurnitureReqDTO reqDTO){
        return ResponseCustom.OK(groupService.addFurniture(reqDTO));
    }

    @Auth
    @PostMapping
    public ResponseCustom<PostGroupResDTO> postGroup(@Valid @RequestBody PostGroupReqDTO reqDTO, @IsLogin LoginStatus loginStatus){
        return ResponseCustom.OK(groupService.postGroup(reqDTO, loginStatus.getUserId()));
    }

    @Auth
    @PostMapping("/member/{inviteCode}")
    public ResponseCustom<Long> joinGroup(@PathVariable("inviteCode") String inviteCode, @IsLogin LoginStatus loginStatus){
        return ResponseCustom.OK(groupService.joinGroup(inviteCode, loginStatus.getUserId()));
    }

    @GetMapping("/furniture/{furnitureName}")
    public ResponseCustom<List<FurnitureResDTO>> getFurnitureList(@PathVariable("furnitureName") String furnitureName){
        return ResponseCustom.OK(groupService.getFurnitureList(furnitureName));
    }

    @PostMapping("/{groupId}/furniture")
    public ResponseCustom<PostRoomResDTO> postRoomDetail(@PathVariable("groupId") Long groupId, @Valid @RequestBody PostRoomReqDTO reqDTO){
        return ResponseCustom.OK(groupService.postRoomDetail(reqDTO, groupId));
    }
}
