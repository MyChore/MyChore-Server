package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.user.UserAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAgreeRepository extends JpaRepository<UserAgree, Long> {
    Optional<UserAgree> findByUserIdAndStatus(Long userId, String status);
}
