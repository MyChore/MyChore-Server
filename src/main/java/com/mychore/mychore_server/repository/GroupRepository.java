package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByInviteCodeAndStatus(String inviteCode, String status);

    Optional<Group> findGroupByIdAndStatus(Long groupId, String status);
}
