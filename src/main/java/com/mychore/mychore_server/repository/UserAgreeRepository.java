package com.mychore.mychore_server.repository;


import com.mychore.mychore_server.entity.user.UserAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAgreeRepository extends JpaRepository<UserAgree, Long> {

}
