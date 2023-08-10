package com.mychore.mychore_server.dto.notification.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NotiReq { // 그룹원 집안일 완료 알림
    private Long groupId;
    private String deviceToken;
}
