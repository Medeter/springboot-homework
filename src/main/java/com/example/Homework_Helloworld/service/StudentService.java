package com.example.Homework_Helloworld.service;

import com.example.Homework_Helloworld.model.Student;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class StudentService {
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("ST_1", 20, 995687881L));
        students.add(new Student("ST_2", 21, 899785241L));
        students.add(new Student("ST_3", 19, 778956491L));
        return students;
    }
}
