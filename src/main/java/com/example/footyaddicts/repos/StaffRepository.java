package com.example.footyaddicts.repos;

import com.example.footyaddicts.models.ERole;
import com.example.footyaddicts.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff , Long> {
    Optional<Staff> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(s) FROM Staff s JOIN s.roles r WHERE r.name = :role")
    Long findSumOfStaffByRole(@Param("role") ERole role);
}
