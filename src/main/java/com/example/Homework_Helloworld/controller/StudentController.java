package com.example.Homework_Helloworld.controller;

import com.example.Homework_Helloworld.model.Student;
import com.example.Homework_Helloworld.model.StudentResponse;
import com.example.Homework_Helloworld.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping("/student")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    
    // Exception Handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StudentResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest().body(new StudentResponse(false, errors));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StudentResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            // Remove method name prefix if present (e.g., "createStudentWithRequestParam.email" -> "email")
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());
        }
        return ResponseEntity.badRequest().body(new StudentResponse(false, errors));
    }

    // 1. POST with @RequestParam (ลำดับที่ 1)
    @PostMapping("/student/request-param")
    public ResponseEntity<StudentResponse> createStudentWithRequestParam(
            @RequestParam @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @RequestParam @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @RequestParam @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @RequestParam @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            Student student = new Student(name, age, phone, email);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "✅ สร้างข้อมูลนักเรียนสำเร็จผ่าน @RequestParam", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }

    // 2. POST with @RequestBody (ลำดับที่ 2)
    @PostMapping("/student/request-body")
    public ResponseEntity<StudentResponse> createStudentWithRequestBody(@Valid @RequestBody Student student) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "✅ สร้างข้อมูลนักเรียนสำเร็จผ่าน @RequestBody", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }

    // 3. POST with @PathVariable (ลำดับที่ 3)
    @PostMapping("/student/path-variable/{name}/{age}/{phone}/{email}")
    public ResponseEntity<StudentResponse> createStudentWithPathVariable(
            @PathVariable @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @PathVariable @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @PathVariable @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @PathVariable @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            Student student = new Student(name, age, phone, email);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "สร้างข้อมูลนักเรียนสำเร็จผ่าน @PathVariable", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }

    // 4. POST with @PathParam (JAX-RS) (ลำดับที่ 4)
    @PostMapping("/student/path-param/{name}/{age}/{phone}/{email}")
    public ResponseEntity<StudentResponse> createStudentWithPathParam(
            @PathParam("name") @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @PathParam("age") @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @PathParam("phone") @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @PathParam("email") @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            Student student = new Student(name, age, phone, email);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "✅ สร้างข้อมูลนักเรียนสำเร็จผ่าน @PathParam (JAX-RS)", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }
}
