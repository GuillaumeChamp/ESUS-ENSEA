package com.example.application.data.repository;

import com.example.application.data.entity.School;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, UUID> {

}
