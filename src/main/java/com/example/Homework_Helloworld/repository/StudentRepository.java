package com.example.Homework_Helloworld.repository;

import com.example.Homework_Helloworld.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // JPA Query Methods สำหรับ filter ตามอายุ
    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findByAgeGreaterThanEqual(int minAge);
    List<Student> findByAgeLessThanEqual(int maxAge);
    List<Student> findByAge(int age);
    
    // Custom Query สำหรับ filter ข้อมูลแบบละเอียด
    @Query("SELECT s FROM Student s WHERE s.age BETWEEN :minAge AND :maxAge ORDER BY s.age ASC")
    List<Student> findStudentsByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    @Query("SELECT s FROM Student s WHERE s.age BETWEEN :minAge AND :maxAge AND s.name LIKE %:name% ORDER BY s.age ASC")
    List<Student> findStudentsByAgeRangeAndName(@Param("minAge") int minAge, @Param("maxAge") int maxAge, @Param("name") String name);
}
