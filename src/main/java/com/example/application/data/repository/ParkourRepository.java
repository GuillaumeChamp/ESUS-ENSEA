package com.example.application.data.repository;

import com.example.application.data.entity.Parkour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkourRepository extends JpaRepository<Parkour,Long> {
    @Query("select p from Parkour p " +
            "where lower(p.semester) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.option_suivi) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(p.major) like lower(concat('%', :searchTerm, '%'))")
    List<Parkour> search(@Param("searchTerm") String searchTerm);
}
