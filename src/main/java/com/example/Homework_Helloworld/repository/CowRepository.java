package com.example.Homework_Helloworld.repository;

import com.example.Homework_Helloworld.model.Cow;
import com.example.Homework_Helloworld.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CowRepository extends JpaRepository<Cow, Long> {
    
    // Find by cow name
    List<Cow> findByCowNameContainingIgnoreCase(String cowName);
    
    // Find by type
    List<Cow> findByTypeContainingIgnoreCase(String type);
    
    // Find by farm
    List<Cow> findByFarm(Farm farm);
    List<Cow> findByFarmId(Long farmId);
    
    // Find by birth date range
    List<Cow> findByBirthBetween(LocalDate startDate, LocalDate endDate);
    List<Cow> findByBirthAfter(LocalDate date);
    List<Cow> findByBirthBefore(LocalDate date);
    
    // Custom query to get cows with farm and owner details
    @Query("SELECT c FROM Cow c " +
           "JOIN FETCH c.farm f " +
           "JOIN FETCH f.owner o " +
           "WHERE f.id = :farmId " +
           "ORDER BY c.cowName ASC")
    List<Cow> findCowsWithFarmAndOwnerByFarmId(@Param("farmId") Long farmId);
    
    // Search cows by name with farm and owner details
    @Query("SELECT c FROM Cow c " +
           "JOIN FETCH c.farm f " +
           "JOIN FETCH f.owner o " +
           "WHERE LOWER(c.cowName) LIKE LOWER(CONCAT('%', :cowName, '%')) " +
           "ORDER BY c.cowName ASC")
    List<Cow> searchCowsWithDetails(@Param("cowName") String cowName);
    
    // Get all cows with farm and owner details
    @Query("SELECT c FROM Cow c " +
           "JOIN FETCH c.farm f " +
           "JOIN FETCH f.owner o " +
           "ORDER BY c.cowName ASC")
    List<Cow> findAllCowsWithDetails();
}
