package com.iptech.jobportal.service;

import org.springframework.transaction.annotation.Transactional;
import com.iptech.jobportal.dto.DashboardStatsResponse;
import com.iptech.jobportal.model.Job;
import com.iptech.jobportal.model.JobRequest;
import com.iptech.jobportal.model.NewsletterSubscriber;
import com.iptech.jobportal.model.User;
import com.iptech.jobportal.repository.ApplicationRepository;
import com.iptech.jobportal.repository.JobRepository;
import com.iptech.jobportal.repository.JobRequestRepository;
import com.iptech.jobportal.repository.NewsletterSubscriberRepository;
import com.iptech.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    private static final String FRONTEND_URL = "http://localhost:5173"; // production mein badalna hoga

    public void submitJobRequest(String companyName, String jobTitle, String jobDescription, String location,
            String salary, String employerEmail, String experience, String workHour,
            String jobType, String skills, MultipartFile image) throws IOException {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image, "uploads");
        }

        JobRequest jobRequest = new JobRequest();
        jobRequest.setCompanyName(companyName);
        jobRequest.setJobTitle(jobTitle);
        jobRequest.setJobDescription(jobDescription);
        jobRequest.setLocation(location);
        jobRequest.setSalary(salary);
        jobRequest.setEmployerEmail(employerEmail);
        jobRequest.setExperience(experience);
        jobRequest.setWorkHour(workHour);
        jobRequest.setJobType(jobType);
        jobRequest.setSkills(skills);
        jobRequest.setImageUrl(imageUrl);
        jobRequest.setStatus(JobRequest.Status.PENDING);

        jobRequestRepository.save(jobRequest);
    }

    public List<JobRequest> getAllJobRequests() {
        return jobRequestRepository.findAll();
    }

    public void approveJobRequest(Long id) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job request not found"));

        Job job = new Job();
        job.setCompanyName(jobRequest.getCompanyName());
        job.setJobTitle(jobRequest.getJobTitle());
        job.setJobDescription(jobRequest.getJobDescription());
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setEmployerEmail(jobRequest.getEmployerEmail());
        job.setImageUrl(jobRequest.getImageUrl());
        job.setExperience(jobRequest.getExperience());
        job.setWorkHour(jobRequest.getWorkHour());
        job.setJobType(jobRequest.getJobType());
        job.setSkills(jobRequest.getSkills());
        job.setApprovedAt(LocalDateTime.now());

        jobRepository.save(job);

        jobRequest.setStatus(JobRequest.Status.APPROVED);
        jobRequestRepository.save(jobRequest);

        // Job live hone ke baad dono ko notify karo
        notifyUsersAboutNewJob(job);
        notifySubscribersAboutNewJob(job);
    }

    // Registered & verified job seekers ko notify karna
    private void notifyUsersAboutNewJob(Job job) {
        String jobLink = FRONTEND_URL + "/jobs/" + job.getId();

        List<User> users = userRepository.findByRoleAndIsVerifiedTrue(User.Role.JOBSEEKER);
        for (User user : users) {
            try {
                String html = emailTemplateService.buildJobPostedTemplate(
                        user.getName(),
                        job.getJobTitle(),
                        job.getCompanyName(),
                        jobLink
                );

                emailService.sendHtmlEmail(
                        user.getEmail(),
                        "New Job Opportunity: " + job.getJobTitle(),
                        html
                );
            } catch (Exception e) {
                System.err.println("Failed to send job alert to " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }

    // Newsletter subscribers ko notify karna
    private void notifySubscribersAboutNewJob(Job job) {
        String jobLink = FRONTEND_URL + "/jobs/" + job.getId();

        List<NewsletterSubscriber> subscribers = newsletterSubscriberRepository.findAll();
        for (NewsletterSubscriber subscriber : subscribers) {
            try {
                String subscriberName = subscriber.getEmail().split("@")[0];
                String unsubscribeLink = FRONTEND_URL + "/unsubscribe?email=" + subscriber.getEmail();

                String html = emailTemplateService.buildNewsletterJobTemplate(
                        subscriberName,
                        job.getJobTitle(),
                        job.getCompanyName(),
                        jobLink,
                        unsubscribeLink
                );

                emailService.sendHtmlEmail(
                        subscriber.getEmail(),
                        "New Job Opening: " + job.getJobTitle(),
                        html
                );
            } catch (Exception e) {
                System.err.println("Failed to send newsletter email to " + subscriber.getEmail() + ": " + e.getMessage());
            }
        }
    }

    public void rejectJobRequest(Long id) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job request not found"));
        jobRequest.setStatus(JobRequest.Status.REJECTED);
        jobRequestRepository.save(jobRequest);
    }

    public void deleteJobRequest(Long id) {
        jobRequestRepository.deleteById(id);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAllByOrderByApprovedAtDesc();
    }

    @Transactional
    public void deleteJob(Long id) {
    applicationRepository.deleteByJobId(id);
    jobRepository.deleteById(id);
}

    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setCompanyName(jobDetails.getCompanyName());
        job.setJobTitle(jobDetails.getJobTitle());
        job.setJobDescription(jobDetails.getJobDescription());
        job.setLocation(jobDetails.getLocation());
        job.setSalary(jobDetails.getSalary());
        return jobRepository.save(job);
    }

    public DashboardStatsResponse getDashboardStats() {
        return new DashboardStatsResponse(
                jobRepository.count(),
                applicationRepository.count(),
                jobRequestRepository.count(),
                newsletterSubscriberRepository.count());
    }

    public Job createJobDirectly(String companyName, String jobTitle, String jobDescription, String location,
            String salary, String experience, String workHour, String jobType, String skills,
            MultipartFile image) throws IOException {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image, "uploads");
        }

        Job job = new Job();
        job.setCompanyName(companyName);
        job.setJobTitle(jobTitle);
        job.setJobDescription(jobDescription);
        job.setLocation(location);
        job.setSalary(salary);
        job.setImageUrl(imageUrl);
        job.setExperience(experience);
        job.setWorkHour(workHour);
        job.setJobType(jobType);
        job.setSkills(skills);
        job.setApprovedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);

        notifyUsersAboutNewJob(savedJob);
        notifySubscribersAboutNewJob(savedJob);

        return savedJob;
    }
}