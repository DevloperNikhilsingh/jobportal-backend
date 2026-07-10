package com.iptech.jobportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_enquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactEnquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String message;

    private String phone;
    private String jobSector;
    private String subject;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    // ---- Naye fields for reply feature ----
    @Column(columnDefinition = "TEXT")
    private String replyMessage;

    private boolean replied = false;

    private LocalDateTime repliedAt;
    // -----------------------------------------

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Role {
        JOBSEEKER, EMPLOYER
    }
}