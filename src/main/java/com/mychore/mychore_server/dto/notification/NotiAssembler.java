package com.mychore.mychore_server.dto.notification;

import com.google.firebase.messaging.*;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotiAssembler {
    public Notification toEntity(String title, String body) {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    public ApnsConfig toEntity() {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setSound("default")
                        .build())
                .build();
    }

    public Message toEntity(Notification notification, ApnsConfig apnsConfig, String deviceToken) {
        return Message.builder()
                .setNotification(notification)
                .setApnsConfig(apnsConfig)
                .setToken(deviceToken)
                .build();
    }

    public MulticastMessage toEntity(Notification notification, ApnsConfig apnsConfig, List<String> deviceTokens) {
        return MulticastMessage.builder()
                .setNotification(notification)
                .setApnsConfig(apnsConfig)
                .addAllTokens(deviceTokens)
                .build();
    }

    public com.mychore.mychore_server.entity.notification.Notification toEntity(User user, Group group, String title, String content) {
        return com.mychore.mychore_server.entity.notification.Notification.builder()
                .user(user)
                .group(group)
                .title(title)
                .content(content)
                .build();
    }
}
