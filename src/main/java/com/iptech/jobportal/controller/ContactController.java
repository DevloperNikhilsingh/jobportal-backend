package com.iptech.jobportal.controller;

import com.iptech.jobportal.model.ContactEnquiry;
import com.iptech.jobportal.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactEnquiry>> getAllEnquiries() {
        return ResponseEntity.ok(contactService.getAllEnquiries());
    }

    @PostMapping
    public ResponseEntity<?> submitContact(@RequestBody ContactRequest request) {
        contactService.submitContact(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getJobSector(),
                request.getSubject(),
                request.getMessage(),
                request.getRole()
        );
        return ResponseEntity.ok().build();
    }

    // Simple inner class matching Contact.jsx form fields
    public static class ContactRequest {
        private String name;
        private String email;
        private String phone;
        private String jobSector;
        private String subject;
        private String message;
        private String role;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getJobSector() { return jobSector; }
        public void setJobSector(String jobSector) { this.jobSector = jobSector; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}