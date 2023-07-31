package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.Group.AddFurnitureResDTO;
import com.mychore.mychore_server.dto.Group.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.PostGroupResDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Gender;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {
    public final GroupService groupService;
    @PostMapping("/add")
    public String addFurniture(@RequestBody AddFurnitureResDTO reqDTO){
        return groupService.addFurniture(reqDTO);
    }

    @Auth
    @PostMapping("/floor")
    public ResponseCustom<PostGroupResDTO> postGroup(@RequestBody PostGroupReqDTO reqDTO, @IsLogin LoginStatus loginStatus){
        return groupService.postGroup(reqDTO, loginStatus.getUserId());
    }
}
