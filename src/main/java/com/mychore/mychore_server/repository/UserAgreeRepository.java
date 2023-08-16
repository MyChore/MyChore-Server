package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.entity.user.UserAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAgreeRepository extends JpaRepository<UserAgree, Long> {
    Optional<UserAgree> findByUserIdAndStatus(Long userId, String status);

    void deleteByUser(User user);
    List<UserAgree> findAllByIsAgreeTodayNotiAndStatus(Boolean isAgreeTodayNoti, String status);
}
