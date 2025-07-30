package com.example.Homework_Helloworld.model;

public class Student {
    private String name;
    private int age;
    private long phone;
    
    public Student() {}
    
    public Student(String name, int age, long phone) {
        this.name = name;
        this.age = age;
        this.phone = phone;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public long getPhone() {
        return phone;
    }
    
    public void setPhone(long phone) {
        this.phone = phone;
    }
}
