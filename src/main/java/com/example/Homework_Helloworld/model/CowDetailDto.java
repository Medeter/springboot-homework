package com.example.Homework_Helloworld.model;

import java.time.LocalDate;

public class CowDetailDto {
    // Cow information
    private Long id;
    private String name;
    private LocalDate birth;
    private String type;
    private int age;
    
    // Farm information (nested object)
    private FarmInfo farm;
    
    // Nested class for farm information
    public static class FarmInfo {
        private Long id;
        private String name;
        private OwnerInfo owner;
        
        // Nested class for owner information
        public static class OwnerInfo {
            private Long id;
            private String name;
            private String surname;
            private String address;
            
            // Constructors
            public OwnerInfo() {}
            
            public OwnerInfo(Long id, String name, String surname, String address) {
                this.id = id;
                this.name = name;
                this.surname = surname;
                this.address = address;
            }
            
            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            
            public String getSurname() { return surname; }
            public void setSurname(String surname) { this.surname = surname; }
            
            public String getAddress() { return address; }
            public void setAddress(String address) { this.address = address; }
            
            public String getFullName() {
                return name + " " + surname;
            }
        }
        
        // Constructors
        public FarmInfo() {}
        
        public FarmInfo(Long id, String name, OwnerInfo owner) {
            this.id = id;
            this.name = name;
            this.owner = owner;
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public OwnerInfo getOwner() { return owner; }
        public void setOwner(OwnerInfo owner) { this.owner = owner; }
    }
    
    // Constructors
    public CowDetailDto() {}
    
    public CowDetailDto(Cow cow) {
        this.id = cow.getId();
        this.name = cow.getCowName();
        this.birth = cow.getBirth();
        this.type = cow.getType();
        this.age = cow.getAge();
        
        if (cow.getFarm() != null) {
            FarmInfo.OwnerInfo ownerInfo = null;
            if (cow.getFarm().getOwner() != null) {
                ownerInfo = new FarmInfo.OwnerInfo(
                    cow.getFarm().getOwner().getId(),
                    cow.getFarm().getOwner().getName(),
                    cow.getFarm().getOwner().getSurName(),
                    cow.getFarm().getOwner().getAddress()
                );
            }
            
            this.farm = new FarmInfo(
                cow.getFarm().getId(),
                cow.getFarm().getName(),
                ownerInfo
            );
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDate getBirth() { return birth; }
    public void setBirth(LocalDate birth) { this.birth = birth; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public FarmInfo getFarm() { return farm; }
    public void setFarm(FarmInfo farm) { this.farm = farm; }
}
