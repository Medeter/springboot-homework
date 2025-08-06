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
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@RestController
@Validated
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
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

    // 1. GET all students
    @GetMapping("/student")
    public ResponseEntity<?> getAllStudents(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        
        try {
            List<Student> students;
            String message;
            
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                students = studentService.getAllStudentsOrderBy(sortBy.trim(), sortDir);
                message = "✅ แสดงนักเรียนทั้งหมดเรียงตาม " + sortBy + " (" + (sortDir != null ? sortDir : "asc") + ")";
            } else {
                students = studentService.getAllStudents();
                message = "✅ แสดงนักเรียนทั้งหมด";
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", message);
            result.put("data", students);
            result.put("count", students.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการดึงข้อมูล: " + e.getMessage()));
        }
    }
    
    // 1.6 GET students by age range (Query Parameters) - Enhanced
    @GetMapping("/student/age")
    public ResponseEntity<?> getStudentsByAge(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Integer exactAge,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        
        try {
            List<Student> students;
            String message;
            
            if (exactAge != null) {
                // ค้นหาตามอายุที่แน่นอน
                students = studentService.findStudentsByExactAge(exactAge);
                message = "✅ ค้นหานักเรียนอายุ " + exactAge + " ปี";
            } else if (minAge != null && maxAge != null) {
                // ค้นหาตามช่วงอายุ
                if (name != null && !name.trim().isEmpty()) {
                    students = studentService.findStudentsByAgeRangeAndName(minAge, maxAge, name.trim());
                    message = "✅ ค้นหานักเรียนอายุ " + minAge + "-" + maxAge + " ปี ที่มีชื่อ '" + name + "'";
                } else {
                    students = studentService.findStudentsByAgeRange(minAge, maxAge);
                    message = "✅ ค้นหานักเรียนอายุ " + minAge + "-" + maxAge + " ปี";
                }
            } else if (minAge != null) {
                // ค้นหาตั้งแต่อายุ xx ขึ้นไป
                students = studentService.findStudentsFromAge(minAge);
                message = "✅ ค้นหานักเรียนอายุ " + minAge + " ปีขึ้นไป";
            } else if (maxAge != null) {
                // ค้นหาอายุไม่เกิน xx ปี
                students = studentService.findStudentsUpToAge(maxAge);
                message = "✅ ค้นหานักเรียนอายุไม่เกิน " + maxAge + " ปี";
            } else {
                // ถ้าไม่มีพารามิเตอร์ ให้แสดงทั้งหมด
                students = studentService.getAllStudents();
                message = "✅ แสดงนักเรียนทั้งหมด";
            }
            
            // เรียงลำดับถ้าระบุ
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                students = applySort(students, sortBy.trim(), sortDir);
                message += " (เรียงตาม " + sortBy + " " + (sortDir != null ? sortDir : "asc") + ")";
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", message);
            result.put("data", students);
            result.put("count", students.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการค้นหา: " + e.getMessage()));
        }
    }
    
    // 1.2 GET students with comprehensive filters
    @GetMapping("/student/filter")
    public ResponseEntity<?> getStudentsWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Integer exactAge,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        
        try {
            List<Student> students;
            StringBuilder messageBuilder = new StringBuilder("✅ ค้นหานักเรียน");
            List<String> filters = new ArrayList<>();
            
            // ใช้ comprehensive filter
            students = studentService.findStudentsWithFilters(
                name != null && !name.trim().isEmpty() ? name.trim() : null,
                email != null && !email.trim().isEmpty() ? email.trim() : null,
                exactAge != null ? exactAge : minAge,
                exactAge != null ? exactAge : maxAge,
                exactAge,
                phone != null && !phone.trim().isEmpty() ? phone.trim() : null
            );
            
            // สร้างข้อความอธิบายการค้นหา
            if (name != null && !name.trim().isEmpty()) {
                filters.add("ชื่อ '" + name + "'");
            }
            if (email != null && !email.trim().isEmpty()) {
                filters.add("อีเมล '" + email + "'");
            }
            if (exactAge != null) {
                filters.add("อายุ " + exactAge + " ปี");
            } else if (minAge != null && maxAge != null) {
                filters.add("อายุ " + minAge + "-" + maxAge + " ปี");
            } else if (minAge != null) {
                filters.add("อายุ " + minAge + " ปีขึ้นไป");
            } else if (maxAge != null) {
                filters.add("อายุไม่เกิน " + maxAge + " ปี");
            }
            if (phone != null && !phone.trim().isEmpty()) {
                filters.add("เบอร์โทร '" + phone + "'");
            }
            
            if (!filters.isEmpty()) {
                messageBuilder.append("ด้วยเงื่อนไข: ").append(String.join(", ", filters));
            } else {
                messageBuilder.append("ทั้งหมด");
            }
            
            // เรียงลำดับถ้าระบุ
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                students = applySort(students, sortBy.trim(), sortDir);
                messageBuilder.append(" (เรียงตาม ").append(sortBy).append(" ").append(sortDir != null ? sortDir : "asc").append(")");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", messageBuilder.toString());
            result.put("data", students);
            result.put("count", students.size());
            result.put("filters_applied", filters);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการค้นหา: " + e.getMessage()));
        }
    }
    
    // 1.3 GET students by name (various search modes)
    @GetMapping("/student/name")
    public ResponseEntity<?> getStudentsByName(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "contains") String mode) {
        
        try {
            List<Student> students;
            String message;
            
            switch (mode.toLowerCase()) {
                case "starts":
                    students = studentService.findStudentsByNameStartingWith(name);
                    message = "✅ ค้นหานักเรียนที่ชื่อขึ้นต้นด้วย '" + name + "'";
                    break;
                case "ends":
                    students = studentService.findStudentsByNameEndingWith(name);
                    message = "✅ ค้นหานักเรียนที่ชื่อลงท้ายด้วย '" + name + "'";
                    break;
                case "contains":
                default:
                    students = studentService.findStudentsByNameContaining(name);
                    message = "✅ ค้นหานักเรียนที่ชื่อมี '" + name + "'";
                    break;
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", message);
            result.put("data", students);
            result.put("count", students.size());
            result.put("search_mode", mode);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการค้นหา: " + e.getMessage()));
        }
    }
    
    // 1.4 GET students by phone
    @GetMapping("/student/phone")
    public ResponseEntity<?> getStudentsByPhone(@RequestParam String phone) {
        try {
            List<Student> students;
            String message;
            
            try {
                // ลองแปลงเป็น long ก่อน (ค้นหาแบบตรงตัว)
                long phoneNumber = Long.parseLong(phone);
                students = studentService.findStudentsByPhone(phoneNumber);
                message = "✅ ค้นหานักเรียนเบอร์โทร " + phone + " (ตรงตัว)";
            } catch (NumberFormatException e) {
                // ถ้าแปลงไม่ได้ ให้ค้นหาแบบบางส่วน
                students = studentService.findStudentsByPhoneContaining(phone);
                message = "✅ ค้นหานักเรียนเบอร์โทรที่มี '" + phone + "'";
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", message);
            result.put("data", students);
            result.put("count", students.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการค้นหา: " + e.getMessage()));
        }
    }
    
    // Helper method สำหรับการเรียงลำดับ
    private List<Student> applySort(List<Student> students, String sortBy, String sortDir) {
        boolean ascending = sortDir == null || !sortDir.equals("desc");
        
        switch (sortBy.toLowerCase()) {
            case "name":
                students.sort((s1, s2) -> ascending ? 
                    s1.getName().compareToIgnoreCase(s2.getName()) : 
                    s2.getName().compareToIgnoreCase(s1.getName()));
                break;
            case "age":
                students.sort((s1, s2) -> ascending ? 
                    Integer.compare(s1.getAge(), s2.getAge()) : 
                    Integer.compare(s2.getAge(), s1.getAge()));
                break;
            case "email":
                students.sort((s1, s2) -> ascending ? 
                    s1.getEmail().compareToIgnoreCase(s2.getEmail()) : 
                    s2.getEmail().compareToIgnoreCase(s1.getEmail()));
                break;
            case "phone":
                students.sort((s1, s2) -> ascending ? 
                    Long.compare(s1.getPhone(), s2.getPhone()) : 
                    Long.compare(s2.getPhone(), s1.getPhone()));
                break;
            case "id":
            default:
                students.sort((s1, s2) -> ascending ? 
                    Long.compare(s1.getId(), s2.getId()) : 
                    Long.compare(s2.getId(), s1.getId()));
                break;
        }
        
        return students;
    }
    
    // 1.5 GET student by ID
    @GetMapping("/student/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id).orElse(null);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new StudentResponse(true, "✅ ดึงข้อมูลนักเรียนสำเร็จ", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการดึงข้อมูล: " + e.getMessage()));
        }
    }
    
    // 1.7 PUT student by ID with RequestBody  
    @PutMapping("/student/{id}")
    public ResponseEntity<StudentResponse> updateStudentById(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        try {
            Student student = studentService.getStudentById(id).orElse(null);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่ (ยกเว้นตัวเอง)
            if (!student.getEmail().equals(studentDetails.getEmail()) && studentService.existsByEmail(studentDetails.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            student.setName(studentDetails.getName());
            student.setAge(studentDetails.getAge());
            student.setPhone(studentDetails.getPhone());
            student.setEmail(studentDetails.getEmail());
            
            Student updatedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(new StudentResponse(true, "✅ อัปเดตข้อมูลนักเรียนสำเร็จ", updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการอัปเดตข้อมูล: " + e.getMessage()));
        }
    }

    // 2.1 POST with @RequestParam
    @PostMapping("/student")
    public ResponseEntity<StudentResponse> createStudentWithRequestParam(
            @RequestParam @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @RequestParam @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @RequestParam @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @RequestParam @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่
            if (studentService.existsByEmail(email)) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            Student student = new Student(name, age, phone, email);
            Student savedStudent = studentService.saveStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "✅ สร้างข้อมูลนักเรียนสำเร็จผ่าน @RequestParam", savedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }

    // 2.2 PUT with @RequestParam
    @PutMapping("/student")
    public ResponseEntity<StudentResponse> updateStudentWithRequestParam(
            @RequestParam Long id,
            @RequestParam @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @RequestParam @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @RequestParam @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @RequestParam @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            Student student = studentService.getStudentById(id).orElse(null);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่ (ยกเว้นตัวเอง)
            if (!student.getEmail().equals(email) && studentService.existsByEmail(email)) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            student.setName(name);
            student.setAge(age);
            student.setPhone(phone);
            student.setEmail(email);
            
            Student updatedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(new StudentResponse(true, "✅ อัปเดตข้อมูลนักเรียนสำเร็จผ่าน @RequestParam", updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการอัปเดตข้อมูล: " + e.getMessage()));
        }
    }

    // 3.1 POST with @RequestBody
    @PostMapping(value = "/student", consumes = "application/json")
    public ResponseEntity<StudentResponse> createStudentWithRequestBody(@Valid @RequestBody Student student) {
        try {
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่
            if (studentService.existsByEmail(student.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            Student savedStudent = studentService.saveStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "✅ สร้างข้อมูลนักเรียนสำเร็จผ่าน @RequestBody", savedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }
    
    // 3.1.1 POST multiple students with @RequestBody (Array)
    @PostMapping(value = "/student/batch", consumes = "application/json")
    public ResponseEntity<?> createMultipleStudentsWithRequestBody(@Valid @RequestBody List<Student> students) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<Student> savedStudents = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                try {
                    // ตรวจสอบว่าอีเมลซ้ำหรือไม่
                    if (studentService.existsByEmail(student.getEmail())) {
                        errors.add("รายการที่ " + (i + 1) + ": อีเมล " + student.getEmail() + " มีในระบบแล้ว");
                        continue;
                    }
                    
                    Student savedStudent = studentService.saveStudent(student);
                    savedStudents.add(savedStudent);
                } catch (Exception e) {
                    errors.add("รายการที่ " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            result.put("success", errors.isEmpty());
            result.put("message", savedStudents.size() + " รายการสำเร็จ" + (errors.isEmpty() ? "" : ", " + errors.size() + " รายการล้มเหลว"));
            result.put("data", savedStudents);
            if (!errors.isEmpty()) {
                result.put("errors", errors);
            }
            result.put("total", students.size());
            result.put("success_count", savedStudents.size());
            result.put("error_count", errors.size());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }

    // 3.2 PUT with @RequestBody
    @PutMapping(value = "/student", consumes = "application/json")
    public ResponseEntity<StudentResponse> updateStudentWithRequestBody(@Valid @RequestBody Student studentDetails) {
        try {
            if (studentDetails.getId() == null) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ กรุณาระบุ ID ของนักเรียน"));
            }
            
            Student student = studentService.getStudentById(studentDetails.getId()).orElse(null);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่ (ยกเว้นตัวเอง)
            if (!student.getEmail().equals(studentDetails.getEmail()) && studentService.existsByEmail(studentDetails.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            student.setName(studentDetails.getName());
            student.setAge(studentDetails.getAge());
            student.setPhone(studentDetails.getPhone());
            student.setEmail(studentDetails.getEmail());
            
            Student updatedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(new StudentResponse(true, "✅ อัปเดตข้อมูลนักเรียนสำเร็จผ่าน @RequestBody", updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการอัปเดตข้อมูล: " + e.getMessage()));
        }
    }

    // 4.1 POST with @PathVariable
    @PostMapping("/student/create/{name}/{age}/{phone}/{email}")
    public ResponseEntity<StudentResponse> createStudentWithPathVariable(
            @PathVariable @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @PathVariable @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @PathVariable @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @PathVariable @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่
            if (studentService.existsByEmail(email)) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            Student student = new Student(name, age, phone, email);
            Student savedStudent = studentService.saveStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentResponse(true, "✅ สร้างข้อมูลนักเรียนสำเร็จผ่าน @PathVariable", savedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการสร้างข้อมูล: " + e.getMessage()));
        }
    }

    // 4.2 PUT with @PathVariable
    @PutMapping("/student/update/{id}/{name}/{age}/{phone}/{email}")
    public ResponseEntity<StudentResponse> updateStudentWithPathVariable(
            @PathVariable Long id,
            @PathVariable @NotBlank(message = "กรุณากรอกชื่อ") @Size(min = 2, max = 50, message = "ชื่อต้องมี 2-50 ตัวอักษร") String name,
            @PathVariable @Min(value = 1, message = "อายุต้องมากกว่า 0") @Max(value = 150, message = "อายุต้องน้อยกว่า 150") int age,
            @PathVariable @Positive(message = "เบอร์โทรไม่ครบ 10 ตัว") long phone,
            @PathVariable @NotBlank(message = "กรุณากรอกอีเมล") @Email(message = "รูปแบบอีเมลไม่ถูกต้อง") String email) {
        
        try {
            Student student = studentService.getStudentById(id).orElse(null);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            
            // ตรวจสอบว่าอีเมลซ้ำหรือไม่ (ยกเว้นตัวเอง)
            if (!student.getEmail().equals(email) && studentService.existsByEmail(email)) {
                return ResponseEntity.badRequest()
                    .body(new StudentResponse(false, "❌ อีเมลนี้มีในระบบแล้ว"));
            }
            
            student.setName(name);
            student.setAge(age);
            student.setPhone(phone);
            student.setEmail(email);
            
            Student updatedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(new StudentResponse(true, "✅ อัปเดตข้อมูลนักเรียนสำเร็จผ่าน @PathVariable", updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการอัปเดตข้อมูล: " + e.getMessage()));
        }
    }
    
    // 5. DELETE student
    @DeleteMapping("/student/{id}")
    public ResponseEntity<StudentResponse> deleteStudent(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id).orElse(null);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            
            studentService.deleteStudent(id);
            return ResponseEntity.ok(new StudentResponse(true, "✅ ลบข้อมูลนักเรียนสำเร็จ"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new StudentResponse(false, "❌ เกิดข้อผิดพลาดในการลบข้อมูล: " + e.getMessage()));
        }
    }
}
