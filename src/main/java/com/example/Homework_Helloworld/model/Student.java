package com.example.Homework_Helloworld.model;

import jakarta.validation.constraints.*;

public class Student {
    @NotBlank(message = "กรุณากรอกชื่อ")
    @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร")
    private String name;
    
    @Min(value = 1, message = "อายุต้องมากกว่า 0")
    @Max(value = 150, message = "อายุต้องน้อยกว่า 150")
    private int age;
    
    @NotNull(message = "กรุณากรอกเบอร์โทร")
    @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว")
    private long phone;
    
    @NotBlank(message = "กรุณากรอกอีเมล")
    @Email(message = "รูปแบบอีเมลไม่ถูกต้อง")
    private String email;
    
    public Student() {}
    
    public Student(String name, int age, long phone, String email) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
