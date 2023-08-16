package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.notification.Notification;
import com.mychore.mychore_server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByUser(User user);
}
