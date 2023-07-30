package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndStatus(Long id, String status);
    Optional<User> findByEmailAndStatus(String email, String status);
    Optional<User> findByEmailAndProviderAndStatus(String email, Provider provider, String status);
    Optional<User> findByNicknameAndStatusAndIdNot(String nickname, String status, Long id);
    Optional<User> findByNicknameAndStatus(String nickname, String status);
}
