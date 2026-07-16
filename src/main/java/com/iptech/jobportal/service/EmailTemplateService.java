package com.iptech.jobportal.service;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    // ================= COMMON HEADER/FOOTER STYLE CONSTANTS =================
    // (inline hi rakha hai kyunki email clients external CSS support nahi karte)

    // ================= 1. JOB POSTED NOTIFICATION (logged-in users ke liye) =================
    public String buildJobPostedTemplate(String userName, String jobTitle, String companyName, String jobLink) {
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0; padding:0; background-color:#f4f5f7; font-family: 'Segoe UI', Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f5f7; padding: 30px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                                <tr>
                                    <td style="background: linear-gradient(90deg,#4338CA,#9333EA); padding: 22px 30px;">
                                        <h1 style="color:#ffffff; font-size:20px; margin:0;">JobPortal</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 30px;">
                                        <p style="font-size:15px; color:#334155; margin:0 0 12px 0;">Hi %s,</p>
                                        <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 20px 0;">
                                            A new job opportunity that matches your profile has just been posted. Check it out before it's gone!
                                        </p>

                                        <table cellpadding="0" cellspacing="0" style="width:100%%; background:#f8f9fc; border-radius:8px; padding:18px; margin-bottom:24px;">
                                            <tr>
                                                <td style="padding:16px;">
                                                    <p style="margin:0 0 4px 0; font-size:16px; font-weight:600; color:#1e293b;">%s</p>
                                                    <p style="margin:0; font-size:14px; color:#64748b;">%s</p>
                                                </td>
                                            </tr>
                                        </table>

                                        <table cellpadding="0" cellspacing="0" style="margin: 0 auto;">
                                            <tr>
                                                <td align="center" style="border-radius:8px; background: linear-gradient(90deg,#4338CA,#9333EA);">
                                                    <a href="%s" style="display:inline-block; padding:12px 28px; font-size:14px; font-weight:600; color:#ffffff; text-decoration:none;">
                                                        View Job & Apply
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 20px 30px; background:#f8f9fc; text-align:center;">
                                        <p style="font-size:12px; color:#94a3b8; margin:0;">
                                            You're receiving this because you have an account on JobPortal.<br/>
                                            &copy; 2026 JobPortal. All rights reserved.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(userName, jobTitle, companyName, jobLink);
    }

    // ================= 2. WELCOME EMAIL (OTP verify hone ke baad) =================
    public String buildWelcomeTemplate(String userName) {
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0; padding:0; background-color:#f4f5f7; font-family: 'Segoe UI', Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f5f7; padding: 30px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                                <tr>
                                    <td style="background: linear-gradient(90deg,#4338CA,#9333EA); padding: 22px 30px;">
                                        <h1 style="color:#ffffff; font-size:20px; margin:0;">JobPortal</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 30px;">
                                        <p style="font-size:15px; color:#334155; margin:0 0 12px 0;">Hi %s,</p>
                                        <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 20px 0;">
                                            Welcome aboard! Your account has been successfully created and verified.
                                            You can now explore job opportunities, apply directly, and track your applications — all in one place.
                                        </p>

                                        <table cellpadding="0" cellspacing="0" style="margin: 0 auto;">
                                            <tr>
                                                <td align="center" style="border-radius:8px; background: linear-gradient(90deg,#4338CA,#9333EA);">
                                                    <a href="https://iptech.netlify.app" style="display:inline-block; padding:12px 28px; font-size:14px; font-weight:600; color:#ffffff; text-decoration:none;">
                                                        Explore Jobs
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 20px 30px; background:#f8f9fc; text-align:center;">
                                        <p style="font-size:12px; color:#94a3b8; margin:0;">
                                            &copy; 2026 JobPortal. All rights reserved.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(userName);
    }

    // ================= 3. APPLICATION SUCCESS (job apply karne ke baad) =================
    public String buildApplicationSuccessTemplate(String userName, String jobTitle, String companyName) {
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0; padding:0; background-color:#f4f5f7; font-family: 'Segoe UI', Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f5f7; padding: 30px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                                <tr>
                                    <td style="background: linear-gradient(90deg,#16a34a,#22c55e); padding: 24px 30px; text-align:center;">
                                        <div style="width:52px; height:52px; background:#ffffff; border-radius:50%%; margin:0 auto 10px auto; line-height:52px; font-size:26px; color:#16a34a;">&#10003;</div>
                                        <h1 style="color:#ffffff; font-size:18px; margin:0;">Application Submitted</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 30px;">
                                        <p style="font-size:15px; color:#334155; margin:0 0 12px 0;">Hi %s,</p>
                                        <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 20px 0;">
                                            Your application has been successfully submitted. The employer has been notified and will review your profile shortly.
                                        </p>

                                        <table cellpadding="0" cellspacing="0" style="width:100%%; background:#f8f9fc; border-radius:8px; padding:18px; margin-bottom:20px;">
                                            <tr>
                                                <td style="padding:16px;">
                                                    <p style="margin:0 0 4px 0; font-size:16px; font-weight:600; color:#1e293b;">%s</p>
                                                    <p style="margin:0; font-size:14px; color:#64748b;">%s</p>
                                                </td>
                                            </tr>
                                        </table>

                                        <p style="font-size:13px; color:#94a3b8; line-height:1.6; margin:0;">
                                            You can track the status of this application anytime from your dashboard.
                                        </p>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 20px 30px; background:#f8f9fc; text-align:center;">
                                        <p style="font-size:12px; color:#94a3b8; margin:0;">
                                            &copy; 2026 JobPortal. All rights reserved.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(userName, jobTitle, companyName);
    }

    // ================= 4. NEWSLETTER NOTIFICATION (subscribers ke liye, job post hone pe) =================
    public String buildNewsletterJobTemplate(String subscriberName, String jobTitle, String companyName, String jobLink, String unsubscribeLink) {
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0; padding:0; background-color:#f4f5f7; font-family: 'Segoe UI', Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f5f7; padding: 30px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                                <tr>
                                    <td style="background: linear-gradient(90deg,#4338CA,#9333EA); padding: 22px 30px;">
                                        <h1 style="color:#ffffff; font-size:20px; margin:0;">JobPortal Newsletter</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 30px;">
                                        <p style="font-size:15px; color:#334155; margin:0 0 12px 0;">Hi %s,</p>
                                        <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 20px 0;">
                                            A new job has just been posted on JobPortal. Since you're subscribed to our updates, thought you'd want to be one of the first to know!
                                        </p>

                                        <table cellpadding="0" cellspacing="0" style="width:100%%; background:#f8f9fc; border-radius:8px; padding:18px; margin-bottom:24px;">
                                            <tr>
                                                <td style="padding:16px;">
                                                    <p style="margin:0 0 4px 0; font-size:16px; font-weight:600; color:#1e293b;">%s</p>
                                                    <p style="margin:0; font-size:14px; color:#64748b;">%s</p>
                                                </td>
                                            </tr>
                                        </table>

                                        <table cellpadding="0" cellspacing="0" style="margin: 0 auto;">
                                            <tr>
                                                <td align="center" style="border-radius:8px; background: linear-gradient(90deg,#4338CA,#9333EA);">
                                                    <a href="https://iptech.netlify.app/" style="display:inline-block; padding:12px 28px; font-size:14px; font-weight:600; color:#ffffff; text-decoration:none;">
                                                        View Job Details
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 20px 30px; background:#f8f9fc; text-align:center;">
                                        <p style="font-size:12px; color:#94a3b8; margin:0 0 6px 0;">
                                            You're receiving this because you subscribed to JobPortal newsletter.
                                        </p>
                                        <a href="%s" style="font-size:12px; color:#6366f1; text-decoration:underline;">Unsubscribe</a>
                                        <p style="font-size:12px; color:#94a3b8; margin:8px 0 0 0;">
                                            &copy; 2026 JobPortal. All rights reserved.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(subscriberName, jobTitle, companyName, jobLink, unsubscribeLink);
    }

    // ================= 5. SHORTLIST + INTERVIEW DETAILS =================
public String buildShortlistTemplate(String applicantName, String jobTitle, String companyName,
                                      String interviewDate, String interviewAddress, String mapLink) {
    return """
        <!DOCTYPE html>
        <html>
        <body style="margin:0; padding:0; background-color:#f4f5f7; font-family: 'Segoe UI', Arial, sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f5f7; padding: 30px 0;">
                <tr>
                    <td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                            <tr>
                                <td style="background: linear-gradient(90deg,#16a34a,#22c55e); padding: 24px 30px; text-align:center;">
                                    <div style="width:52px; height:52px; background:#ffffff; border-radius:50%%; margin:0 auto 10px auto; line-height:52px; font-size:26px; color:#16a34a;">&#127942;</div>
                                    <h1 style="color:#ffffff; font-size:18px; margin:0;">You've Been Shortlisted!</h1>
                                </td>
                            </tr>

                            <tr>
                                <td style="padding: 30px;">
                                    <p style="font-size:15px; color:#334155; margin:0 0 12px 0;">Hi %s,</p>
                                    <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 20px 0;">
                                        Congratulations! We're pleased to inform you that you have been shortlisted for the position below. Please find your interview details:
                                    </p>

                                    <table cellpadding="0" cellspacing="0" style="width:100%%; background:#f8f9fc; border-radius:8px; padding:18px; margin-bottom:20px;">
                                        <tr>
                                            <td style="padding:16px;">
                                                <p style="margin:0 0 4px 0; font-size:16px; font-weight:600; color:#1e293b;">%s</p>
                                                <p style="margin:0; font-size:14px; color:#64748b;">%s</p>
                                            </td>
                                        </tr>
                                    </table>

                                    <table cellpadding="0" cellspacing="0" style="width:100%%; border:1px solid #e2e8f0; border-radius:8px; margin-bottom:20px;">
                                        <tr>
                                            <td style="padding:18px;">
                                                <p style="margin:0 0 10px 0; font-size:13px; color:#94a3b8; text-transform:uppercase; letter-spacing:0.5px; font-weight:600;">Interview Schedule</p>

                                                <p style="margin:0 0 12px 0; font-size:14px; color:#334155;">
                                                    <strong style="color:#1e293b;">Date:</strong> %s
                                                </p>

                                                <p style="margin:0; font-size:14px; color:#334155; line-height:1.6;">
                                                    <strong style="color:#1e293b;">Venue:</strong><br/>
                                                    %s
                                                </p>

                                                <table cellpadding="0" cellspacing="0" style="margin-top:14px;">
                                                    <tr>
                                                        <td style="border-radius:6px; border:1px solid #16a34a;">
                                                            <a href="%s" style="display:inline-block; padding:9px 18px; font-size:13px; font-weight:600; color:#16a34a; text-decoration:none;">
                                                                View on Google Maps
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>

                                    <p style="font-size:13px; color:#94a3b8; line-height:1.6; margin:0;">
                                        Please carry a copy of your resume and a valid ID proof. If you have any questions or need to reschedule, feel free to reach out to us.
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td style="padding: 20px 30px; background:#f8f9fc; text-align:center;">
                                    <p style="font-size:12px; color:#94a3b8; margin:0;">
                                        &copy; 2026 JobPortal. All rights reserved.
                                    </p>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """.formatted(applicantName, jobTitle, companyName, interviewDate, interviewAddress, mapLink);
}

// ================= 6. APPLICATION REJECTED =================
public String buildRejectionTemplate(String applicantName, String jobTitle, String companyName) {
    return """
        <!DOCTYPE html>
        <html>
        <body style="margin:0; padding:0; background-color:#f4f5f7; font-family: 'Segoe UI', Arial, sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f5f7; padding: 30px 0;">
                <tr>
                    <td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                            <tr>
                                <td style="background: linear-gradient(90deg,#475569,#64748b); padding: 22px 30px;">
                                    <h1 style="color:#ffffff; font-size:20px; margin:0;">JobPortal</h1>
                                </td>
                            </tr>

                            <tr>
                                <td style="padding: 30px;">
                                    <p style="font-size:15px; color:#334155; margin:0 0 12px 0;">Dear %s,</p>
                                    <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 16px 0;">
                                        Thank you for taking the time to apply for the position below and for your interest in joining our team.
                                    </p>

                                    <table cellpadding="0" cellspacing="0" style="width:100%%; background:#f8f9fc; border-radius:8px; padding:18px; margin-bottom:20px;">
                                        <tr>
                                            <td style="padding:16px;">
                                                <p style="margin:0 0 4px 0; font-size:16px; font-weight:600; color:#1e293b;">%s</p>
                                                <p style="margin:0; font-size:14px; color:#64748b;">%s</p>
                                            </td>
                                        </tr>
                                    </table>

                                    <p style="font-size:15px; color:#334155; line-height:1.6; margin:0 0 16px 0;">
                                        After careful consideration, we have decided to move forward with other candidates whose profiles more closely match our current requirements. This was not an easy decision, as we received applications from many qualified candidates.
                                    </p>

                                    <p style="font-size:15px; color:#334155; line-height:1.6; margin:0;">
                                        We truly appreciate the effort you put into your application and encourage you to apply for future openings that match your skills and experience. We wish you the very best in your job search.
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td style="padding: 20px 30px; background:#f8f9fc; text-align:center;">
                                    <p style="font-size:12px; color:#94a3b8; margin:0;">
                                        &copy; 2026 JobPortal. All rights reserved.
                                    </p>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """.formatted(applicantName, jobTitle, companyName);
}
}