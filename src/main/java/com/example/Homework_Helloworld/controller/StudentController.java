package com.example.Homework_Helloworld.controller;

import com.example.Homework_Helloworld.model.Student;
import com.example.Homework_Helloworld.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping("/student")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
}
