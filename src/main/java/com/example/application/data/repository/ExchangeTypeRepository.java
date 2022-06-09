package com.example.application.data.repository;

import com.example.application.data.entity.ExchangeType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeTypeRepository extends JpaRepository<ExchangeType, Long> {

}
