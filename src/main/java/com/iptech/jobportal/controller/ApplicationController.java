package com.iptech.jobportal.controller;

import com.iptech.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply-job")
    public ResponseEntity<String> applyForJob(
            @RequestParam("jobId") Long jobId,
            @RequestParam("applicantName") String applicantName,
            @RequestParam("applicantEmail") String applicantEmail,
            @RequestParam("applicantPhone") String applicantPhone,
            @RequestParam(value = "coverLetter", required = false) String coverLetter,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "experience", required = false) String experience,
            @RequestParam(value = "workHour", required = false) String workHour,
            @RequestParam(value = "jobType", required = false) String jobType,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "applyDate", required = false) String applyDate,
            @RequestParam(value = "qualification", required = false) String qualification,
            @RequestParam("resume") MultipartFile resume) {
        try {
            applicationService.applyForJob(
                    jobId, applicantName, applicantEmail, applicantPhone, coverLetter,
                    address, experience, workHour, jobType, skills, location, applyDate, qualification,
                    resume);
            return ResponseEntity.ok("Applied successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
