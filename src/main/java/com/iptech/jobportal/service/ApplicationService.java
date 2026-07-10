package com.iptech.jobportal.service;

import com.iptech.jobportal.model.Application;
import com.iptech.jobportal.model.Job;
import com.iptech.jobportal.repository.ApplicationRepository;
import com.iptech.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    private static final String INTERVIEW_ADDRESS =
            "IPTECH DIGITAL SOLUTIONS, 4/68-D-3, SA, Daulatpur Rd, near IDBI Bank, Pandeypur, Paharia, Varanasi, Uttar Pradesh 221002";

    private static final String INTERVIEW_MAP_LINK =
            "https://www.google.com/maps/dir//IPTECH+DIGITAL+SOLUTIONS,+4%2F68-D-3,+SA,+Daulatpur+Rd,+near+IDBI+Bank,+Pandeypur,+Paharia,+Varanasi,+Uttar+Pradesh+221002";

    public void applyForJob(Long jobId, String applicantName, String applicantEmail, String applicantPhone,
                            String coverLetter, String address, String experience, String workHour,
                            String jobType, String skills, String location, String applyDate,
                            String qualification, MultipartFile resume) throws IOException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String resumeUrl = null;
        if (resume != null && !resume.isEmpty()) {
            resumeUrl = fileStorageService.storeFile(resume, "resumes");
        }

        Application application = new Application();
        application.setJob(job);
        application.setApplicantName(applicantName);
        application.setApplicantEmail(applicantEmail);
        application.setApplicantPhone(applicantPhone);
        application.setCoverLetter(coverLetter);
        application.setResumeUrl(resumeUrl);
        application.setAddress(address);
        application.setExperience(experience);
        application.setWorkHour(workHour);
        application.setJobType(jobType);
        application.setSkills(skills);
        application.setLocation(location);
        application.setApplyDate(applyDate);
        application.setQualification(qualification);

        applicationRepository.save(application);

        try {
            String html = emailTemplateService.buildApplicationSuccessTemplate(
                    applicantName,
                    job.getJobTitle(),
                    job.getCompanyName()
            );

            emailService.sendHtmlEmail(
                    applicantEmail,
                    "Application Submitted Successfully",
                    html
            );
        } catch (Exception e) {
            System.err.println("Failed to send application confirmation email to " + applicantEmail + ": " + e.getMessage());
        }
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application shortlistApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(Application.Status.SHORTLISTED);
        Application saved = applicationRepository.save(application);

        String jobTitle = application.getJob() != null ? application.getJob().getJobTitle() : "the position";
        String companyName = application.getJob() != null ? application.getJob().getCompanyName() : "";

        // Shortlist hone ke din se +3 din ka interview date
        LocalDate interviewDate = LocalDate.now().plusDays(3);
        String formattedDate = interviewDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"));

        try {
            String html = emailTemplateService.buildShortlistTemplate(
                    application.getApplicantName(),
                    jobTitle,
                    companyName,
                    formattedDate,
                    INTERVIEW_ADDRESS,
                    INTERVIEW_MAP_LINK
            );

            emailService.sendHtmlEmail(
                    application.getApplicantEmail(),
                    "You've been Shortlisted! Interview Details Inside",
                    html
            );
        } catch (Exception e) {
            System.err.println("Failed to send shortlist email to " + application.getApplicantEmail() + ": " + e.getMessage());
        }

        return saved;
    }

    public Application rejectApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(Application.Status.REJECTED);
        Application saved = applicationRepository.save(application);

        String jobTitle = application.getJob() != null ? application.getJob().getJobTitle() : "the position";
        String companyName = application.getJob() != null ? application.getJob().getCompanyName() : "";

        try {
            String html = emailTemplateService.buildRejectionTemplate(
                    application.getApplicantName(),
                    jobTitle,
                    companyName
            );

            emailService.sendHtmlEmail(
                    application.getApplicantEmail(),
                    "Application Update",
                    html
            );
        } catch (Exception e) {
            System.err.println("Failed to send rejection email to " + application.getApplicantEmail() + ": " + e.getMessage());
        }

        return saved;
    }
}