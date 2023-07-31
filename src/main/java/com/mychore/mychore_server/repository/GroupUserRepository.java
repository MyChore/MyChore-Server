package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
}
