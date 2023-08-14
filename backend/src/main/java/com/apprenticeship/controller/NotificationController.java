package com.apprenticeship.controller;

import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.Member;
import com.apprenticeship.requestsAndResponses.NotificationRequest;
import com.apprenticeship.service.MeetingService;
import com.apprenticeship.service.MemberService;
import com.apprenticeship.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apprenticeship/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final MeetingService meetingService;
    private final MemberService memberService;

    public NotificationController(NotificationService notificationService,
                                  MeetingService meetingService,
                                  MemberService memberService) {
        this.notificationService = notificationService;
        this.meetingService = meetingService;
        this.memberService = memberService;
    }

    /** According to meeting id and member emails, send notification emails.
     * @param notificationRequest The request body contains meeting id and member emails.
     * @return If failed to send emails, return send failed message. Otherwise, return send success message.
     */
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendNotificationEmail(@RequestBody NotificationRequest notificationRequest) {
        try {
            // get meeting object from meeting id in the request body
            Integer meetingId = notificationRequest.meetingId();
            MeetingTable meetingTable = meetingService.getMeetingInfoById(meetingId);

            // get member object from member emails and send emails
            List<String> memberEmails = notificationRequest.memberEmails();
            for (String memberEmail : memberEmails) {
                Member member = memberService.getMemberByMemberEmail(memberEmail);
                // send email
                notificationService.sendEmail(member, meetingTable);
            }
            return ResponseEntity.ok().body("Send notification emails successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send notification emails.");
        }

    }
}
