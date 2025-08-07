package com.example.Homework_Helloworld.service;

import com.example.Homework_Helloworld.model.Farm;
import com.example.Homework_Helloworld.model.Owner;
import com.example.Homework_Helloworld.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmService {
    
    @Autowired
    private FarmRepository farmRepository;
    
    // Basic CRUD operations
    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }
    
    public Optional<Farm> getFarmById(Long id) {
        return farmRepository.findById(id);
    }
    
    public Farm saveFarm(Farm farm) {
        return farmRepository.save(farm);
    }
    
    public void deleteFarm(Long id) {
        farmRepository.deleteById(id);
    }
    
    // Search operations
    public List<Farm> findFarmsByName(String name) {
        return farmRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Farm> findFarmsByOwner(Owner owner) {
        return farmRepository.findByOwner(owner);
    }
    
    public List<Farm> findFarmsByOwnerId(Long ownerId) {
        return farmRepository.findByOwnerId(ownerId);
    }
    
    public List<Farm> findFarmsOrderByCowCount() {
        return farmRepository.findFarmsOrderByCowCount();
    }
}
