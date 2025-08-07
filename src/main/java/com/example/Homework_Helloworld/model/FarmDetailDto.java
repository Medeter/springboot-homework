package com.example.Homework_Helloworld.model;

import java.util.List;

public class FarmDetailDto {
    private Long id;
    private String name;
    private OwnerInfo owner;
    private List<CowInfo> cows;
    private int cowCount;
    
    // Nested class for owner information
    public static class OwnerInfo {
        private Long id;
        private String name;
        private String surname;
        private String address;
        
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
    
    // Nested class for cow information (simplified)
    public static class CowInfo {
        private Long id;
        private String name;
        private String birth;
        private String type;
        private int age;
        
        public CowInfo() {}
        
        public CowInfo(Cow cow) {
            this.id = cow.getId();
            this.name = cow.getCowName();
            this.birth = cow.getBirth().toString();
            this.type = cow.getType();
            this.age = cow.getAge();
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getBirth() { return birth; }
        public void setBirth(String birth) { this.birth = birth; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
    
    // Constructors
    public FarmDetailDto() {}
    
    public FarmDetailDto(Farm farm, List<Cow> cows) {
        this.id = farm.getId();
        this.name = farm.getName();
        
        if (farm.getOwner() != null) {
            this.owner = new OwnerInfo(
                farm.getOwner().getId(),
                farm.getOwner().getName(),
                farm.getOwner().getSurName(),
                farm.getOwner().getAddress()
            );
        }
        
        if (cows != null) {
            this.cows = cows.stream()
                .map(CowInfo::new)
                .toList();
            this.cowCount = cows.size();
        } else {
            this.cowCount = 0;
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public OwnerInfo getOwner() { return owner; }
    public void setOwner(OwnerInfo owner) { this.owner = owner; }
    
    public List<CowInfo> getCows() { return cows; }
    public void setCows(List<CowInfo> cows) { this.cows = cows; }
    
    public int getCowCount() { return cowCount; }
    public void setCowCount(int cowCount) { this.cowCount = cowCount; }
}
