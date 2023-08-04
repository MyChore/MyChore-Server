package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.chore.Chore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChoreRepository extends JpaRepository<Chore, Long>, ChoreRepositoryCustom {

    Optional<Chore> findChoreByIdAndStatus(Long userId, String status);

}
