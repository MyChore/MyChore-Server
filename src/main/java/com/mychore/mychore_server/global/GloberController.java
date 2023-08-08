package com.mychore.mychore_server.global;

import com.mychore.mychore_server.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
@RequiredArgsConstructor
public class GloberController {
    @GetMapping
    public BaseResponse<String> getHello(){
        return new BaseResponse<>("hello!");
    }
}
