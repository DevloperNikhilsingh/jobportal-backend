package com.iptech.jobportal.service;

import com.iptech.jobportal.model.ContactEnquiry;
import com.iptech.jobportal.repository.ContactEnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactEnquiryRepository contactEnquiryRepository;

    @Autowired
    private EmailService emailService;

    public void submitContact(String name, String email, String phone, String jobSector,
                               String subject, String message, String role) {
        ContactEnquiry enquiry = new ContactEnquiry();
        enquiry.setName(name);
        enquiry.setEmail(email);
        enquiry.setPhone(phone);
        enquiry.setJobSector(jobSector);
        enquiry.setSubject(subject);
        enquiry.setMessage(message);
        enquiry.setRole(ContactEnquiry.Role.valueOf(role.toUpperCase()));
        contactEnquiryRepository.save(enquiry);
    }

    public List<ContactEnquiry> getAllEnquiries() {
        return contactEnquiryRepository.findAll();
    }

    public void deleteEnquiry(Long id) {
        contactEnquiryRepository.deleteById(id);
    }

    // ---- Naya method ----
    public ContactEnquiry replyToEnquiry(Long id, String replyText) {
        ContactEnquiry enquiry = contactEnquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + id));

        String subject = "Reply to your enquiry: " + enquiry.getSubject();
        String body = "Hi " + enquiry.getName() + ",\n\n"
                + replyText
                + "\n\n---\nYour original message:\n" + enquiry.getMessage()
                + "\n\nRegards,\nIPTech Job Portal Team";

        emailService.sendEmail(enquiry.getEmail(), subject, body);

        enquiry.setReplyMessage(replyText);
        enquiry.setReplied(true);
        enquiry.setRepliedAt(LocalDateTime.now());

        return contactEnquiryRepository.save(enquiry);
    }
}