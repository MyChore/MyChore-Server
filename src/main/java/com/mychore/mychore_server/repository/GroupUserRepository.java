package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    Optional<GroupUser> findByUserAndGroupAndStatus(User user, Group group, String status);
    List<GroupUser> findGroupUsersByGroupAndStatus(Group group, String status);
    List<GroupUser> findByUserAndRoleAndStatus(User user, Role role, String status);
    List<GroupUser> findByUserAndStatus(User user, String status);

    Optional<GroupUser> findByUserAndGroupAndRoleAndStatus(User user, Group group, Role role, String status);

    Optional<GroupUser> findGroupUserByUserIdAndGroupIdAndStatus(Long userId, Long groupId, String status);

    void deleteByUser(User user);
    void deleteByGroup(Group group);
}
