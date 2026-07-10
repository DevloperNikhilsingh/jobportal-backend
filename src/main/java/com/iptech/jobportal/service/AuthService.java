package com.iptech.jobportal.service;

import com.iptech.jobportal.dto.*;
import com.iptech.jobportal.model.User;
import com.iptech.jobportal.repository.UserRepository;
import com.iptech.jobportal.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String otp = emailService.generateOtp();
        LocalDateTime otpExpiry = LocalDateTime.now().plusMinutes(10);

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setOtp(otp);
        user.setOtpExpiry(otpExpiry);
        user.setIsVerified(false);

        userRepository.save(user);

        emailService.sendEmail(request.getEmail(), "OTP for Registration", "Your OTP is: " + otp);
    }

    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setIsVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return new AuthResponse(null, user.getRole().name(), "OTP verified successfully");
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (!user.getIsVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), "Login successful");
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        String otp = emailService.generateOtp();
        LocalDateTime otpExpiry = LocalDateTime.now().plusMinutes(10);

        user.setOtp(otp);
        user.setOtpExpiry(otpExpiry);
        userRepository.save(user);

        emailService.sendEmail(request.getEmail(), "OTP for Password Reset", "Your OTP is: " + otp);
    }

    public void resetPassword(ResetPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }

    public AuthResponse adminLogin(LoginRequest request) {
        if (!request.getEmail().equals(adminEmail) || !request.getPassword().equals(adminPassword)) {
            throw new RuntimeException("Invalid admin credentials");
        }

        // Check if admin user exists in DB, if not create it
        Optional<User> optionalAdmin = userRepository.findByEmail(adminEmail);
        User admin;
        if (optionalAdmin.isEmpty()) {
            admin = new User();
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ADMIN);
            admin.setIsVerified(true);
            userRepository.save(admin);
        } else {
            admin = optionalAdmin.get();
        }

        String token = jwtUtil.generateToken(adminEmail, "ADMIN");
        return new AuthResponse(token, "ADMIN", "Admin login successful");
    }
}