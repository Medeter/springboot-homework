package com.example.Homework_Helloworld.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Entity
@Table(name = "cows")
public class Cow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "กรุณากรอกชื่อวัว")
    @Size(min = 2, max = 50, message = "ชื่อวัวต้องมี 2-50 ตัวอักษร")
    @Column(name = "cow_name", nullable = false)
    private String cowName;
    
    @NotNull(message = "กรุณากรอกวันเกิด")
    @Column(name = "birth", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    
    @NotBlank(message = "กรุณากรอกประเภทวัว")
    @Size(min = 2, max = 50, message = "ประเภทวัวต้องมี 2-50 ตัวอักษร")
    @Column(name = "type", nullable = false)
    private String type;
    
    // Many Cows can belong to one Farm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id", nullable = false)
    @JsonBackReference("farm-cows")
    private Farm farm;
    
    // Constructors
    public Cow() {}
    
    public Cow(String cowName, LocalDate birth, String type, Farm farm) {
        this.cowName = cowName;
        this.birth = birth;
        this.type = type;
        this.farm = farm;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCowName() {
        return cowName;
    }
    
    public void setCowName(String cowName) {
        this.cowName = cowName;
    }
    
    public LocalDate getBirth() {
        return birth;
    }
    
    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Farm getFarm() {
        return farm;
    }
    
    public void setFarm(Farm farm) {
        this.farm = farm;
    }
    
    // Helper method to calculate age
    public int getAge() {
        if (birth != null) {
            return LocalDate.now().getYear() - birth.getYear();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "Cow{" +
                "id=" + id +
                ", cowName='" + cowName + '\'' +
                ", birth=" + birth +
                ", type='" + type + '\'' +
                ", farm=" + (farm != null ? farm.getName() : "null") +
                '}';
    }
}
