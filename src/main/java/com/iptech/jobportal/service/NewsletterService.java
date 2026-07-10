package com.iptech.jobportal.service;

import com.iptech.jobportal.model.NewsletterSubscriber;
import com.iptech.jobportal.repository.NewsletterSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;

    @Autowired
    private EmailService emailService;

    public void subscribe(String email) {
        if (newsletterSubscriberRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already subscribed");
        }
        NewsletterSubscriber subscriber = new NewsletterSubscriber();
        subscriber.setEmail(email);
        newsletterSubscriberRepository.save(subscriber);

        // ---- Naya: admin ko notification email ----
        emailService.sendEmail(
                "nikhilsingh404444@gmail.com",
                "New Newsletter Subscriber",
                "A new user has subscribed to the newsletter:\n" +
                        "Email: " + email
        );
    }

    public List<NewsletterSubscriber> getAllSubscribers() {
        return newsletterSubscriberRepository.findAll();
    }
}