package com.iptech.jobportal.controller;


import com.iptech.jobportal.dto.DashboardStatsResponse;
import com.iptech.jobportal.model.Application;
import com.iptech.jobportal.model.ContactEnquiry;
import com.iptech.jobportal.model.Job;
import com.iptech.jobportal.model.JobRequest;
import com.iptech.jobportal.model.NewsletterSubscriber;
import com.iptech.jobportal.service.ApplicationService;
import com.iptech.jobportal.service.ContactService;
import com.iptech.jobportal.service.JobService;
import com.iptech.jobportal.service.NewsletterService;
import com.iptech.jobportal.service.PlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private PlacementService placementService;

    @GetMapping("/dashboard-stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(jobService.getDashboardStats());
    }

    @GetMapping("/want-to-hire-requests")
    public ResponseEntity<List<JobRequest>> getAllJobRequests() {
        return ResponseEntity.ok(jobService.getAllJobRequests());
    }

    @PostMapping("/approve-job/{id}")
    public ResponseEntity<String> approveJobRequest(@PathVariable Long id) {
        try {
            jobService.approveJobRequest(id);
            return ResponseEntity.ok("Job request approved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reject-job/{id}")
    public ResponseEntity<String> rejectJobRequest(@PathVariable Long id) {
        try {
            jobService.rejectJobRequest(id);
            return ResponseEntity.ok("Job request rejected");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/want-to-hire-requests/{id}")
public ResponseEntity<String> deleteJobRequest(@PathVariable Long id) {
    try {
        jobService.deleteJobRequest(id);
        return ResponseEntity.ok("Job request deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.ok("Job deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        try {
            return ResponseEntity.ok(jobService.updateJob(id, jobDetails));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @PutMapping("/applications/{id}/shortlist")
    public ResponseEntity<Application> shortlistApplication(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(applicationService.shortlistApplication(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<Application> rejectApplication(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(applicationService.rejectApplication(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/contact-enquiries")
    public ResponseEntity<List<ContactEnquiry>> getAllEnquiries() {
        return ResponseEntity.ok(contactService.getAllEnquiries());
    }

     @PutMapping("/contact-enquiries/{id}/reply")
    public ResponseEntity<?> replyToEnquiry(@PathVariable Long id, @RequestBody ReplyRequest request) {
        try {
            ContactEnquiry updated = contactService.replyToEnquiry(id, request.getReplyMessage());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // ---- Reply request body ke liye inner class ----
    public static class ReplyRequest {
        private String replyMessage;
        public String getReplyMessage() { return replyMessage; }
        public void setReplyMessage(String replyMessage) { this.replyMessage = replyMessage; }
    }

    @DeleteMapping("/contact-enquiries/{id}")
public ResponseEntity<String> deleteEnquiry(@PathVariable Long id) {
    try {
        contactService.deleteEnquiry(id);
        return ResponseEntity.ok("Enquiry deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @GetMapping("/newsletter-subscribers")
    public ResponseEntity<List<NewsletterSubscriber>> getAllSubscribers() {
        return ResponseEntity.ok(newsletterService.getAllSubscribers());
    }

    // ---- Naye Placement endpoints ----
    @PostMapping("/placements")
    public ResponseEntity<?> addPlacement(
            @RequestParam("studentName") String studentName,
            @RequestParam("companyName") String companyName,
            @RequestParam("jobRole") String jobRole,
            @RequestParam("packageCtc") String packageCtc,
            @RequestParam("batchYear") String batchYear,
            @RequestParam("location") String location,
            @RequestParam("testimonial") String testimonial,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            return ResponseEntity.ok(placementService.addPlacement(
                    studentName, companyName, jobRole, packageCtc, batchYear, location, testimonial, image));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/placements/{id}")
    public ResponseEntity<String> deletePlacement(@PathVariable Long id) {
        try {
            placementService.deletePlacement(id);
            return ResponseEntity.ok("Placement deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}