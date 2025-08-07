package com.example.Homework_Helloworld.service;

import com.example.Homework_Helloworld.model.Cow;
import com.example.Homework_Helloworld.model.Farm;
import com.example.Homework_Helloworld.repository.CowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowService {
    
    @Autowired
    private CowRepository cowRepository;
    
    // Basic CRUD operations
    public List<Cow> getAllCows() {
        return cowRepository.findAll();
    }
    
    public Optional<Cow> getCowById(Long id) {
        return cowRepository.findById(id);
    }
    
    public Cow saveCow(Cow cow) {
        return cowRepository.save(cow);
    }
    
    public void deleteCow(Long id) {
        cowRepository.deleteById(id);
    }
    
    // Search operations
    public List<Cow> findCowsByName(String cowName) {
        return cowRepository.findByCowNameContainingIgnoreCase(cowName);
    }
    
    public List<Cow> findCowsByType(String type) {
        return cowRepository.findByTypeContainingIgnoreCase(type);
    }
    
    public List<Cow> findCowsByFarm(Farm farm) {
        return cowRepository.findByFarm(farm);
    }
    
    public List<Cow> findCowsByFarmId(Long farmId) {
        return cowRepository.findByFarmId(farmId);
    }
    
    // Date range operations
    public List<Cow> findCowsByBirthRange(LocalDate startDate, LocalDate endDate) {
        return cowRepository.findByBirthBetween(startDate, endDate);
    }
    
    public List<Cow> findCowsBornAfter(LocalDate date) {
        return cowRepository.findByBirthAfter(date);
    }
    
    public List<Cow> findCowsBornBefore(LocalDate date) {
        return cowRepository.findByBirthBefore(date);
    }
    
    // Business logic methods for requirements
    
    // 1. Get all cows in a farm with full details (farm and owner)
    public List<Cow> getCowsInFarmWithDetails(Long farmId) {
        return cowRepository.findCowsWithFarmAndOwnerByFarmId(farmId);
    }
    
    // 2. Search cows by name with full details
    public List<Cow> searchCowsWithDetails(String cowName) {
        if (cowName == null || cowName.trim().isEmpty()) {
            return cowRepository.findAllCowsWithDetails();
        }
        return cowRepository.searchCowsWithDetails(cowName.trim());
    }
    
    // Get all cows with full details
    public List<Cow> getAllCowsWithDetails() {
        return cowRepository.findAllCowsWithDetails();
    }
}
