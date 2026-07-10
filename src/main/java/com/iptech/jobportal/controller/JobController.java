package com.iptech.jobportal.controller;

import com.iptech.jobportal.model.Job;
import com.iptech.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/want-to-hire")
    public ResponseEntity<String> submitJobRequest(
            @RequestParam("companyName") String companyName,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("location") String location,
            @RequestParam("salary") String salary,
            @RequestParam("employerEmail") String employerEmail,
            @RequestParam(value = "experience", required = false) String experience,
            @RequestParam(value = "workHour", required = false) String workHour,
            @RequestParam(value = "jobType", required = false) String jobType,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            jobService.submitJobRequest(companyName, jobTitle, jobDescription, location, salary,
                    employerEmail, experience, workHour, jobType, skills, image);
            return ResponseEntity.ok("Your job post request submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}