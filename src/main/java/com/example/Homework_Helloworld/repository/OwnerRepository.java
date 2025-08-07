package com.example.Homework_Helloworld.repository;

import com.example.Homework_Helloworld.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    
    // Find by name
    List<Owner> findByNameContainingIgnoreCase(String name);
    List<Owner> findBySurNameContainingIgnoreCase(String surName);
    
    // Find by full name
    @Query("SELECT o FROM Owner o WHERE LOWER(CONCAT(o.name, ' ', o.surName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Owner> findByFullNameContaining(@Param("fullName") String fullName);
    
    // Find by address
    List<Owner> findByAddressContainingIgnoreCase(String address);
}
