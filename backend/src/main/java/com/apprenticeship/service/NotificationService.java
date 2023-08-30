package com.apprenticeship.service;

import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.Member;
import com.apprenticeship.model.Notification;
import com.apprenticeship.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * This class is responsible for sending emails.
 */
@Service
@Transactional
public class NotificationService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailSender;
    private final NotificationRepository notificationRepository;

    public NotificationService(JavaMailSender javaMailSender, NotificationRepository notificationRepository) {
        this.javaMailSender = javaMailSender;
        this.notificationRepository = notificationRepository;
    }


    /**
     * This method is used to send attendees reminder emails that they have new minutes to review.
     * The emails include the meeting topic, meeting name, meeting date, meeting duration, and meeting description.
     *
     * @param memberEmail  The email address of the recipient
     * @param meetingTable The meeting table object
     */
    public void sendEmail(Member memberEmail, MeetingTable meetingTable) {
        // Get the recipient email from the member table
        String recipientEmail = memberEmail.getEmail();
        try {
            // Create HTML email and set the sender email, recipient email, subject, and content
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setFrom(emailSender);
            mimeMessageHelper.setTo(recipientEmail);
            String emailSubject = "New Minutes of " + meetingTable.getMeetingName() + " to Review";
            mimeMessageHelper.setSubject(emailSubject);
            String content =
                    "<html><body>"
                    + "<p>Hi,</p>"
                    + "<p>You have new minutes to review.</p>"
                    + "<p><b>Meeting Topic:<b> " + meetingTable.getMeetingTopic() + "</p>"
                    + "<p><b>Meeting Name:<b> " + meetingTable.getMeetingName() + "</p>"
                    + "<p><b>Meeting Date:<b> " + meetingTable.getMeetingDate() + "</p>"
                    + "<p><b>Meeting Duration:<b> " + meetingTable.getMeetingDuration() + "</p>"
                    + "<p><b>Meeting Description:<b> " + meetingTable.getMeetingDescription() + "</p>"
                    + "<p>Thanks,</p>"
                    + "<p>Apprenticeship Team</p>"
                    + "</body></html>";
            mimeMessageHelper.setText(content, true);

            // Send the email
            javaMailSender.send(mimeMessage);

            // save the email record to notification table
            saveNotification(meetingTable, memberEmail, emailSubject, content, 1);
        } catch (MessagingException e) {
            saveNotification(
                    meetingTable, memberEmail, "Email Sending Failed", e.getMessage(), 0
            );
        }
    }


    /**
     * Save the notification record in the database
     */
    private void saveNotification(MeetingTable meetingTable,
                                  Member memberEmail,
                                  String subject,
                                  String content,
                                  Integer notificationStatus) {
        // Get the current date
        Date currentDate = new Date();


        Notification notification = new Notification(
                meetingTable,
                memberEmail,
                subject,
                content,
                currentDate,
                notificationStatus
        );
        notificationRepository.save(notification);
    }
}
