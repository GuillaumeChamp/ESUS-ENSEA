package com.example.application.data.repository;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("select c from Country c " +
            "where lower(c.country_name) like lower(concat('%', :searchTerm, '%')) ")
    List<Country> search(@Param("searchTerm") String searchTerm);
}
