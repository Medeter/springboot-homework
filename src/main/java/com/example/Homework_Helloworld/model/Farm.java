package com.example.Homework_Helloworld.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "farms")
public class Farm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "กรุณากรอกชื่อฟาร์ม")
    @Size(min = 2, max = 100, message = "ชื่อฟาร์มต้องมี 2-100 ตัวอักษร")
    @Column(name = "name", nullable = false)
    private String name;
    
    // Many Farms can belong to one Owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonBackReference("owner-farms")
    private Owner owner;
    
    // One Farm can have many Cows
    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("farm-cows")
    private List<Cow> cows;
    
    // Constructors
    public Farm() {}
    
    public Farm(String name, Owner owner) {
        this.name = name;
        this.owner = owner;
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
    
    public Owner getOwner() {
        return owner;
    }
    
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    
    public List<Cow> getCows() {
        return cows;
    }
    
    public void setCows(List<Cow> cows) {
        this.cows = cows;
    }
    
    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + (owner != null ? owner.getFullName() : "null") +
                '}';
    }
}
