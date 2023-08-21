package com.apprenticeship.controller;

import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.Member;
import com.apprenticeship.requestsAndResponses.NotificationRequest;
import com.apprenticeship.service.MeetingService;
import com.apprenticeship.service.MemberService;
import com.apprenticeship.service.NotificationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NotificationControllerTest {

    @Autowired
    private NotificationController notificationController;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testSendNotificationEmail_Success() throws MessagingException, Exception {
        // simulate the behavior of the meetingService
        MeetingTable meetingTable = new MeetingTable(
                // set meetingTable's properties
                1,
                "meetingTopic",
                "meetingName",
                new Date(),
                "1 hour",
                "send notification email test"
        );
        when(meetingService.getMeetingInfoById(anyInt())).thenReturn(meetingTable);

        // simulate the behavior of the memberService
        Member member = new Member(
                // set member's properties
                1,
                "Yvo",
                "Wang",
                "yvo123@gmail.com",
                "password",
                "MENTOR"
        );
        when(memberService.getMemberByMemberEmail(anyString())).thenReturn(member);

        // When
        // Create a notification request including meeting id and member emails
        NotificationRequest notificationRequest =
                new NotificationRequest(
                        1,
                        Arrays.asList("kx412764776@gmail.com", "2766657c@student.gla.ac.uk")
                );

        // call the controller method to send notification emails
        ResponseEntity<?> responseEntity = notificationController.sendNotificationEmail(notificationRequest);

        // Then

        // verify the notificationService is called
        verify(notificationService, times(2)).sendEmail(any(Member.class), any(MeetingTable.class));

        // verify the meetingService is called
        verify(meetingService, times(1)).getMeetingInfoById(anyInt());

        // verify the memberService is called
        verify(memberService, times(2)).getMemberByMemberEmail(anyString());

        // Create a mock mvc to send a post request
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        // send a post request to the notification controller to send notification emails
        mockMvc.perform(post("/apprenticeship/notification/sendEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"meetingId\":1,\"memberEmails\":["
                        + "\"kx412764776@gmail.com\", "
                        + "\"2766657c@student.gla.ac.uk\"]}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Send notification emails successfully."));


    }
}