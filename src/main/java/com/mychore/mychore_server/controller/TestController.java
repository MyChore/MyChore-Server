package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.ResponseCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
@RequiredArgsConstructor
public class TestController {
    @ResponseBody
    @GetMapping
    public ResponseCustom<String> testAPI() {
        return ResponseCustom.OK("성공 해써용");
    }
}