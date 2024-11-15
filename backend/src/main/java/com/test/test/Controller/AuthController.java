package com.test.test.Controller;

import com.test.test.DTO.AuthResponse;
import com.test.test.DTO.LoginRequest;
import com.test.test.DTO.RegisterRequest;
import com.test.test.Entity.Role;
import com.test.test.Entity.SecurityInformation;
import com.test.test.Entity.User;
import com.test.test.Repository.SecurityInformationRepository;
import com.test.test.Repository.UserRepository;
import com.test.test.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityInformationRepository repository;

    @GetMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("membershipId") String membershipId,
            @RequestParam("password") String password) {

        System.out.println("Received login request");
        System.out.println("Membership ID: " + membershipId);
        System.out.println("Password: " + password);

        // Check if repository is finding the record
        SecurityInformation user = repository.findByMembershipId(membershipId);
        System.out.println("User found in database: " + (user != null));

        if (user != null) {
            System.out.println("Stored encoded password in DB: " + user.getPassword());
            System.out.println("Plain text password from request: " + password);

            // Check if password matches the encoded password in the database
            boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password match result: " + isPasswordMatch);

            if (isPasswordMatch) {
                System.out.println("Login successful");
                return ResponseEntity.ok("Login successful");
            } else {
                System.out.println("Password does not match");
            }
        } else {
            System.out.println("No user found with the given Membership ID");
        }

        // Default response for invalid credentials
        return ResponseEntity.status(401).body("Invalid Member ID or Password");
    }

}
