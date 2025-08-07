package com.example.Homework_Helloworld.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "owners")
public class Owner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "กรุณากรอกชื่อ")
    @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "กรุณากรอกนามสกุล")
    @Size(min = 2, max = 50, message = "นามสกุลต้องมี 2-50 ตัวอักษร")
    @Column(name = "sur_name", nullable = false)
    private String surName;
    
    @NotBlank(message = "กรุณากรอกที่อยู่")
    @Size(max = 255, message = "ที่อยู่ต้องไม่เกิน 255 ตัวอักษร")
    @Column(name = "address", nullable = false)
    private String address;
    
    // One Owner can have many Farms
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("owner-farms")
    private List<Farm> farms;
    
    // Constructors
    public Owner() {}
    
    public Owner(String name, String surName, String address) {
        this.name = name;
        this.surName = surName;
        this.address = address;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurName() {
        return surName;
    }
    
    public void setSurName(String surName) {
        this.surName = surName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public List<Farm> getFarms() {
        return farms;
    }
    
    public void setFarms(List<Farm> farms) {
        this.farms = farms;
    }
    
    // Helper method to get full name
    public String getFullName() {
        return name + " " + surName;
    }
    
    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
