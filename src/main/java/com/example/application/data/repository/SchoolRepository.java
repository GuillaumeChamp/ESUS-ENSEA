package com.example.application.data.repository;

import com.example.application.data.entity.School;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SchoolRepository extends JpaRepository<School, UUID> {
    @Query("select s from School s " +
            "where lower(s.name) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(s.country.country_name) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(s.city) like lower(concat('%', :searchTerm, '%'))")
    List<School> search(@Param("searchTerm") String searchTerm);
}
