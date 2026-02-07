package com.example.mos.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        
        // 既存のハッシュとの比較
        String existingHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye1J8BcJjdQpNCOqYJRdAOiKEMg8I8jSG";
        boolean matches = encoder.matches(rawPassword, existingHash);
        System.out.println("\nExisting Hash: " + existingHash);
        System.out.println("Matches: " + matches);
    }
}
