package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.Group.AddFurnitureResDTO;
import com.mychore.mychore_server.dto.Group.PostGroupReqDTO;
import com.mychore.mychore_server.dto.Group.PostGroupResDTO;
import com.mychore.mychore_server.dto.ResponseCustom;
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

    @PostMapping("/room")
    public ResponseCustom<PostGroupResDTO> postGroup(@RequestBody PostGroupReqDTO reqDTO){
        return groupService.postGroup(reqDTO);
    }
}
