package com.example.Homework_Helloworld.repository;

import com.example.Homework_Helloworld.model.Farm;
import com.example.Homework_Helloworld.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
    
    // Find by name
    List<Farm> findByNameContainingIgnoreCase(String name);
    
    // Find by owner
    List<Farm> findByOwner(Owner owner);
    List<Farm> findByOwnerId(Long ownerId);
    
    // Find farms with cow count
    @Query("SELECT f FROM Farm f LEFT JOIN f.cows c GROUP BY f.id ORDER BY COUNT(c) DESC")
    List<Farm> findFarmsOrderByCowCount();
}
