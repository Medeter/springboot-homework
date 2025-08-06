package com.example.Homework_Helloworld.service;

import com.example.Homework_Helloworld.model.Student;
import com.example.Homework_Helloworld.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    // Methods สำหรับ filter ตามอายุ
    public List<Student> findStudentsByAgeRange(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }
    
    public List<Student> findStudentsFromAge(int minAge) {
        return studentRepository.findByAgeGreaterThanEqual(minAge);
    }
    
    public List<Student> findStudentsUpToAge(int maxAge) {
        return studentRepository.findByAgeLessThanEqual(maxAge);
    }
    
    public List<Student> findStudentsByExactAge(int age) {
        return studentRepository.findByAge(age);
    }
    
    public List<Student> findStudentsByAgeRangeCustom(int minAge, int maxAge) {
        return studentRepository.findStudentsByAgeRange(minAge, maxAge);
    }
    
    public List<Student> findStudentsByAgeRangeAndName(int minAge, int maxAge, String name) {
        return studentRepository.findStudentsByAgeRangeAndName(minAge, maxAge, name);
    }
    
    // Methods สำหรับ filter ตามชื่อ
    public List<Student> findStudentsByNameContaining(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Student> findStudentsByNameStartingWith(String name) {
        return studentRepository.findByNameStartingWithIgnoreCase(name);
    }
    
    public List<Student> findStudentsByNameEndingWith(String name) {
        return studentRepository.findByNameEndingWithIgnoreCase(name);
    }
    
    // Methods สำหรับ filter ตามโทรศัพท์
    public List<Student> findStudentsByPhone(long phone) {
        return studentRepository.findByPhone(phone);
    }
    
    public List<Student> findStudentsByPhoneContaining(String phone) {
        return studentRepository.findByPhoneContaining(phone);
    }
    
    // Method สำหรับ filter แบบซับซ้อน
    public List<Student> findStudentsWithFilters(String name, String email, Integer minAge, Integer maxAge, Integer exactAge, String phone) {
        return studentRepository.findStudentsWithFilters(name, email, minAge, maxAge, exactAge, phone);
    }
    
    // Methods สำหรับการเรียงลำดับ
    public List<Student> getAllStudentsOrderBy(String sortBy, String sortDir) {
        if (sortDir == null || (!sortDir.equals("asc") && !sortDir.equals("desc"))) {
            sortDir = "asc";
        }
        
        switch (sortBy) {
            case "name":
                return sortDir.equals("asc") ? studentRepository.findAllOrderByNameAsc() : studentRepository.findAllOrderByNameDesc();
            case "age":
                return sortDir.equals("asc") ? studentRepository.findAllOrderByAgeAsc() : studentRepository.findAllOrderByAgeDesc();
            case "id":
                return sortDir.equals("asc") ? studentRepository.findAllOrderByIdAsc() : studentRepository.findAllOrderByIdDesc();
            default:
                return studentRepository.findAllOrderByIdAsc();
        }
    }
}
