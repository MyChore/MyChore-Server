package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.Furniture;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
}
