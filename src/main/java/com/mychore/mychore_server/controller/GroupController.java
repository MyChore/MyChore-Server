package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.GroupListInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Req.UpdateRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Res.*;
import com.mychore.mychore_server.dto.BaseResponse;
import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.GroupService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {
    public final GroupService groupService;
    @PostMapping("/add")
    public BaseResponse<Furniture> addFurniture(@Valid @RequestBody AddFurnitureReqDTO reqDTO){
        return new BaseResponse<>(groupService.addFurniture(reqDTO));
    }

    @Auth
    @PostMapping
    public BaseResponse<PostGroupResDTO> postGroup(@Valid @RequestBody PostGroupReqDTO reqDTO, @IsLogin LoginStatus loginStatus){
        return new BaseResponse<>(groupService.postGroup(reqDTO, loginStatus.getUserId()));
    }

    @Auth
    @PostMapping("/member/{inviteCode}")
    public BaseResponse<Long> joinGroup(@PathVariable("inviteCode") String inviteCode, @IsLogin LoginStatus loginStatus)throws IOException {
        return new BaseResponse<>(groupService.joinGroup(inviteCode, loginStatus.getUserId()));
    }

    @GetMapping("/furniture")
    public BaseResponse<List<FurnitureResDTO>> getFurnitureList(@RequestParam("furnitureTypeName") String furnitureTypeName){
        return new BaseResponse<>(groupService.getFurnitureList(furnitureTypeName));
    }

    @PostMapping("/{groupId}")
    public BaseResponse<PostRoomResDTO> postRoomDetail(@PathVariable("groupId") Long groupId, @Valid @RequestBody PostRoomReqDTO reqDTO){
        return new BaseResponse<>(groupService.postRoomDetail(reqDTO, groupId));
    }

    @Auth
    @GetMapping("/{groupId}")
    public BaseResponse<StaticDataResDTO> getStaticData(@PathVariable("groupId") Long groupId, @IsLogin LoginStatus loginStatus){
        return new BaseResponse<>(groupService.getStaticData(groupId, loginStatus.getUserId()));
    }

    @Auth
    @GetMapping
    public BaseResponse<List<GroupListInfoDTO>> getGroupInfoList(@IsLogin LoginStatus loginStatus){
        return new BaseResponse<>(groupService.getGroupInfoList(loginStatus.getUserId()));
    }

    @Auth
    @PatchMapping("/{groupId}")
    public BaseResponse<StaticDataResDTO> updateGroupName(@PathVariable("groupId") Long groupId, @RequestParam("newName") String newName, @IsLogin LoginStatus loginStatus){
        return new BaseResponse<>(groupService.updateGroupName(groupId, newName, loginStatus.getUserId()));
    }

    @Auth
    @PutMapping("/{groupId}")
    public BaseResponse<StaticDataResDTO> updateGroupFurniture(@PathVariable("groupId") Long groupId, @Valid @RequestBody UpdateRoomReqDTO reqDTO, @IsLogin LoginStatus loginStatus){
        return new BaseResponse<>(groupService.updateGroupFurniture(groupId, reqDTO, loginStatus.getUserId()));
    }

    @Auth
    @DeleteMapping("/{groupId}/withdraw/{userId}")
    public BaseResponse<Void> withdrawUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId, @IsLogin LoginStatus loginStatus){
        groupService.withdrawUser(groupId, userId, loginStatus.getUserId());
        return new BaseResponse<>();
    }

    @Auth
    @DeleteMapping("/{groupId}/member")
    public BaseResponse<Void> deleteUser(@PathVariable("groupId") Long groupId, @IsLogin LoginStatus loginStatus){
        groupService.deleteUser(groupId, loginStatus.getUserId());
        return new BaseResponse<>();
    }

    @Auth
    @DeleteMapping("/{groupId}")
    public BaseResponse<Void> deleteGroup(@PathVariable("groupId") Long groupId, @IsLogin LoginStatus loginStatus) throws IOException {
        groupService.deleteGroup(groupId, loginStatus.getUserId());
        return new BaseResponse<>();
    }

}
