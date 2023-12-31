package com.mychore.mychore_server.dto.chore.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RemainChoreResDTO {
    private Long roomId;
    private Integer remainChores;

    @Builder
    public RemainChoreResDTO(Long roomId, Integer remainChores){
        this.roomId = roomId;
        this.remainChores = remainChores;
    }
}
