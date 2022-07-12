package com.application.data.repository;

import com.application.data.entity.Triggers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TriggersRepository extends JpaRepository<Triggers, UUID> {
}
