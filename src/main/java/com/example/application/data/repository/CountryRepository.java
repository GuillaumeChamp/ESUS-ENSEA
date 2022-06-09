package com.example.application.data.repository;

import com.example.application.data.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, Long> {
}
