package com.example.Homework_Helloworld.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.Homework_Helloworld.model.User;
import com.example.Homework_Helloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // เรียกครั้งแรก
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
    }

    // วิธีแก้หากมีการเรียกซ้ำ - cache ผลลัพธ์
    private User cachedUser;
    private String cachedUsername;

    public User findByUsername(String username) {
        if (username.equals(cachedUsername)) {
            return cachedUser;
        }
        cachedUser = userRepository.findByUsername(username); // เรียกครั้งเดียว
        cachedUsername = username;
        return cachedUser;
    }

    public User save(User user) {
        // เข้ารหัสรหัสผ่านก่อนบันทึก
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}