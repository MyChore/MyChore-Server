package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.Group.Req.PostRoomReqDTO;
import com.mychore.mychore_server.dto.Group.Res.AddFurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Req.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.Res.FurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostRoomResDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {
    public final GroupService groupService;
    @PostMapping("/add")
    public ResponseCustom<Furniture> addFurniture(@RequestBody AddFurnitureResDTO reqDTO){
        return groupService.addFurniture(reqDTO);
    }

    @Auth
    @PostMapping("/floor")
    public ResponseCustom<PostGroupResDTO> postGroup(@RequestBody PostGroupReqDTO reqDTO, @IsLogin LoginStatus loginStatus){
        return groupService.postGroup(reqDTO, loginStatus.getUserId());
    }

    @Auth
    @PostMapping("/member/{inviteCode}")
    public ResponseCustom<Long> joinGroup(@PathVariable("inviteCode") String inviteCode, @IsLogin LoginStatus loginStatus){
        return groupService.joinGroup(inviteCode, loginStatus.getUserId());
    }

    @GetMapping("/furniture/list")
    public ResponseCustom<List<FurnitureResDTO>> getFurnitureList(){
        return groupService.getFurnitureList();
    }

    @PostMapping("/furniture")
    public ResponseCustom<PostRoomResDTO> postRoomDetail(@RequestBody PostRoomReqDTO reqDTO){
        return groupService.postRoomDetail(reqDTO);
    }
}
