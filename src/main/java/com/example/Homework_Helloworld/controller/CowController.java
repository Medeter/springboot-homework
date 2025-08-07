package com.example.Homework_Helloworld.controller;

import com.example.Homework_Helloworld.model.*;
import com.example.Homework_Helloworld.service.CowService;
import com.example.Homework_Helloworld.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CowController {
    
    @Autowired
    private CowService cowService;
    
    @Autowired
    private FarmService farmService;
    
    // Requirement 1: GET /getCowList/{farm} 
    // เรียกดูรายชื่อวัวทั้งหมดในฟาร์ม แสดงข้อมูลฟาร์มแล้วแสดงวัวในฟาร์ม
    @GetMapping("/getCowList/{farm}")
    public ResponseEntity<?> getCowListByFarm(@PathVariable("farm") Long farmId) {
        try {
            // ดึงข้อมูลฟาร์ม
            Farm farm = farmService.getFarmById(farmId).orElse(null);
            if (farm == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "❌ ไม่พบฟาร์ม ID " + farmId);
                return ResponseEntity.notFound().build();
            }
            
            // ดึงข้อมูลวัวในฟาร์ม
            List<Cow> cows = cowService.getCowsInFarmWithDetails(farmId);
            
            // สร้าง FarmDetailDto
            FarmDetailDto farmDetail = new FarmDetailDto(farm, cows);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "✅ ดึงข้อมูลฟาร์ม '" + farm.getName() + "' และวัวในฟาร์มสำเร็จ");
            result.put("farm", farmDetail);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new CowResponse(false, "❌ เกิดข้อผิดพลาดในการดึงข้อมูลวัว: " + e.getMessage()));
        }
    }
    
    // Requirement 2: POST /cowSearch
    // ค้นหาชื่อวัวทั้งหมดในระบบ แสดงข้อมูลวัวและฟาร์ม
    @PostMapping("/cowSearch")
    public ResponseEntity<?> searchCows(@RequestBody Map<String, String> searchRequest) {
        try {
            String cowName = searchRequest.get("cowName");
            
            List<Cow> cows = cowService.searchCowsWithDetails(cowName);
            
            // แปลงเป็น DTO เพื่อแสดงข้อมูลครบถ้วน
            List<CowDetailDto> cowDetails = cows.stream()
                .map(CowDetailDto::new)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("cows", cowDetails);
            result.put("count", cowDetails.size());
            result.put("searchTerm", cowName);
            
            if (cowName == null || cowName.trim().isEmpty()) {
                result.put("message", "✅ แสดงวัวทั้งหมดในระบบพร้อมข้อมูลฟาร์ม");
            } else {
                result.put("message", cowDetails.isEmpty() ? 
                    "ℹ️ ไม่พบวัวที่มีชื่อ '" + cowName + "'" : 
                    "✅ พบวัวที่มีชื่อ '" + cowName + "' จำนวน " + cowDetails.size() + " ตัว");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new CowResponse(false, "❌ เกิดข้อผิดพลาดในการค้นหาวัว: " + e.getMessage()));
        }
    }
    
    // Additional endpoints for basic CRUD operations
    
    // GET all cows - ดูวัวทั้งหมด แสดงข้อมูลวัวและฟาร์ม
    @GetMapping("/cows")
    public ResponseEntity<?> getAllCows() {
        try {
            List<Cow> cows = cowService.getAllCowsWithDetails();
            
            // แปลงเป็น DTO เพื่อแสดงข้อมูลครบถ้วน
            List<CowDetailDto> cowDetails = cows.stream()
                .map(CowDetailDto::new)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "✅ ดึงข้อมูลวัวทั้งหมดพร้อมข้อมูลฟาร์มสำเร็จ");
            result.put("cows", cowDetails);
            result.put("count", cowDetails.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new CowResponse(false, "❌ เกิดข้อผิดพลาดในการดึงข้อมูล: " + e.getMessage()));
        }
    }
    
    // GET cow by ID
    @GetMapping("/cow/{id}")
    public ResponseEntity<CowResponse> getCowById(@PathVariable Long id) {
        try {
            Cow cow = cowService.getCowById(id).orElse(null);
            if (cow == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new CowResponse(true, "✅ ดึงข้อมูลวัวสำเร็จ", cow));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new CowResponse(false, "❌ เกิดข้อผิดพลาดในการดึงข้อมูล: " + e.getMessage()));
        }
    }
    
    // DELETE cow
    @DeleteMapping("/cow/{id}")
    public ResponseEntity<CowResponse> deleteCow(@PathVariable Long id) {
        try {
            Cow cow = cowService.getCowById(id).orElse(null);
            if (cow == null) {
                return ResponseEntity.notFound().build();
            }
            
            cowService.deleteCow(id);
            return ResponseEntity.ok(new CowResponse(true, "✅ ลบข้อมูลวัวสำเร็จ"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new CowResponse(false, "❌ เกิดข้อผิดพลาดในการลบข้อมูล: " + e.getMessage()));
        }
    }
}
