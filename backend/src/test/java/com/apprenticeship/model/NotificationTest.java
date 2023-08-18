package com.apprenticeship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private Notification notification;
    @BeforeEach
    void setUp() {
        notification = new Notification();
    }

    @Test
    void testConstructorAndGetter() {
        Date date = new Date();
        notification = new Notification(
                1,
                new MeetingTable(),
                new Member(),
                "notification subject",
                "notification content",
                date,
                1
        );
        assertThat(notification.getNotificationId()).isEqualTo(1);
        assertThat(notification.getMeetingId()).isNotNull();
        assertThat(notification.getMemberEmail()).isNotNull();
        assertThat(notification.getNotificationSubject()).isEqualTo("notification subject");
        assertThat(notification.getNotificationContent()).isEqualTo("notification content");
        assertThat(notification.getNotificationPostDate()).isEqualTo(date);
        assertThat(notification.getNotificationStatus()).isEqualTo(1);

        Notification notification2 = new Notification(
                new MeetingTable(),
                new Member(),
                "new notification subject",
                "new notification content",
                date,
                1
        );

        assertThat(notification2.getMeetingId()).isNotNull();
        assertThat(notification2.getMemberEmail()).isNotNull();
        assertThat(notification2.getNotificationSubject()).isEqualTo("new notification subject");
        assertThat(notification2.getNotificationContent()).isEqualTo("new notification content");
        assertThat(notification2.getNotificationPostDate()).isEqualTo(date);
        assertThat(notification2.getNotificationStatus()).isEqualTo(1);

    }

    @Test
    void testSetters() {
        Date date = new Date();
        notification.setNotificationId(1);
        notification.setMeetingId(new MeetingTable());
        notification.setMemberEmail(new Member());
        notification.setNotificationSubject("notification subject");
        notification.setNotificationContent("notification content");
        notification.setNotificationPostDate(date);
        notification.setNotificationStatus(1);

        assertThat(notification.getNotificationId()).isEqualTo(1);
        assertThat(notification.getMeetingId()).isNotNull();
        assertThat(notification.getMemberEmail()).isNotNull();
        assertThat(notification.getNotificationSubject()).isEqualTo("notification subject");
        assertThat(notification.getNotificationContent()).isEqualTo("notification content");
        assertThat(notification.getNotificationPostDate()).isEqualTo(date);
        assertThat(notification.getNotificationStatus()).isEqualTo(1);
    }

}