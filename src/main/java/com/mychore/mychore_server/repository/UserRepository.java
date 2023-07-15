package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
