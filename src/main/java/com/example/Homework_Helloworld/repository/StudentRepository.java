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
    
    // JPA Query Methods สำหรับ filter ตามชื่อ
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByNameStartingWithIgnoreCase(String name);
    List<Student> findByNameEndingWithIgnoreCase(String name);
    
    // JPA Query Methods สำหรับ filter ตามโทรศัพท์
    List<Student> findByPhone(long phone);
    
    // Custom Query สำหรับค้นหาโทรศัพท์แบบบางส่วน (แปลง long เป็น string ก่อน)
    @Query("SELECT s FROM Student s WHERE CAST(s.phone AS string) LIKE %:phone%")
    List<Student> findByPhoneContaining(@Param("phone") String phone);
    
    // Custom Queries สำหรับ filter แบบซับซ้อน
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:minAge IS NULL OR s.age >= :minAge) AND " +
           "(:maxAge IS NULL OR s.age <= :maxAge) AND " +
           "(:exactAge IS NULL OR s.age = :exactAge) AND " +
           "(:phone IS NULL OR CAST(s.phone AS string) LIKE CONCAT('%', :phone, '%')) " +
           "ORDER BY s.id ASC")
    List<Student> findStudentsWithFilters(
        @Param("name") String name,
        @Param("email") String email,
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge,
        @Param("exactAge") Integer exactAge,
        @Param("phone") String phone
    );
    
    // Query สำหรับการเรียงลำดับ
    @Query("SELECT s FROM Student s ORDER BY s.name ASC")
    List<Student> findAllOrderByNameAsc();
    
    @Query("SELECT s FROM Student s ORDER BY s.name DESC")
    List<Student> findAllOrderByNameDesc();
    
    @Query("SELECT s FROM Student s ORDER BY s.age ASC")
    List<Student> findAllOrderByAgeAsc();
    
    @Query("SELECT s FROM Student s ORDER BY s.age DESC")
    List<Student> findAllOrderByAgeDesc();
    
    @Query("SELECT s FROM Student s ORDER BY s.id ASC")
    List<Student> findAllOrderByIdAsc();
    
    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findAllOrderByIdDesc();
}
