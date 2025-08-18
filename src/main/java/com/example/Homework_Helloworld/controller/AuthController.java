package com.example.Homework_Helloworld.controller;

import com.example.Homework_Helloworld.model.LoginRequest;
import com.example.Homework_Helloworld.model.User;
import com.example.Homework_Helloworld.service.UserService;
import com.example.Homework_Helloworld.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
// import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Missing or invalid Authorization header: กรุณาเข้าสู่ระบบก่อนใช้งาน"
                ));
            }
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "User not found: กรุณาเข้าสู่ระบบก่อนใช้งาน"
                ));
            }
            // ไม่ควรส่ง password กลับไป
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("role", user.getRole());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Invalid token: กรุณาเข้าสู่ระบบก่อนใช้งาน " + e.getMessage()
            ));
        }
    }
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Incorrect username or password"
            ));
        }

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("role", userDetails.getAuthorities().stream().findFirst().orElse(null).getAuthority());
        result.put("username", userDetails.getUsername());
        result.put("token", jwt);
        result.put("tokenType", "Bearer");
        // ไม่คืน password
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            // สร้าง JWT token ให้ user ที่สมัครใหม่
            final String jwt = jwtUtil.generateToken(savedUser.getUsername());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("id", savedUser.getId());
            result.put("role", savedUser.getRole());
            result.put("username", savedUser.getUsername());
            result.put("token", jwt);
            result.put("tokenType", "Bearer");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Registration failed: " + e.getMessage()
            ));
        }
    }
}