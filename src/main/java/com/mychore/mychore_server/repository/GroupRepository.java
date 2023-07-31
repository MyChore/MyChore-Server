package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByInviteCode(String inviteCode);
}
