package com.apprenticeship.model;

import jakarta.persistence.*;

/**
 * Notification entity class to be used for sending notifications to members of the meeting.
 */

@Entity
@Table(
        name = "notification",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "notification_id_unique",
                        columnNames = "notificationId"
                )
        }
)
public class Notification {

    @Id
    @SequenceGenerator(
            name = "notification_id_seq",
            sequenceName = "notification_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_seq"
    )
    private Integer notificationId;

    @JoinColumn(
            name = "meetingId",
            referencedColumnName = "meetingId",
            foreignKey = @ForeignKey(
                    name = "meetingId_fk"
            ),
            nullable = false
    )
    @ManyToOne
    private MeetingTable meetingId;

    @JoinColumn(
            name = "memberEmail",
            referencedColumnName = "email",
            foreignKey = @ForeignKey(
                    name = "memberEmail_fk"
            ),
            nullable = false
    )
    @ManyToOne
    private Member memberEmail;

    @Column(
            nullable = false
    )
    private String notificationSubject;

    @Column(
            nullable = false
    )
    private String notificationContent;

    @Column(
            nullable = false
    )
    private String notificationPostDate;

    // notificationStatus is if the notification has been sent successfully or not
    // 0 = not sent, 1 = sent
    @Column(
            nullable = false
    )
    private Integer notificationStatus;

    public Notification() {
    }

    public Notification(Integer notificationId,
                        MeetingTable meetingId,
                        Member memberEmail,
                        String notificationSubject,
                        String notificationContent,
                        String notificationPostDate,
                        Integer notificationStatus) {
        this.notificationId = notificationId;
        this.meetingId = meetingId;
        this.memberEmail = memberEmail;
        this.notificationSubject = notificationSubject;
        this.notificationContent = notificationContent;
        this.notificationPostDate = notificationPostDate;
        this.notificationStatus = notificationStatus;
    }

    public Notification (MeetingTable meetingId,
                         Member memberEmail,
                         String notificationSubject,
                         String notificationContent,
                         String notificationPostDate,
                         Integer notificationStatus) {
        this.meetingId = meetingId;
        this.memberEmail = memberEmail;
        this.notificationSubject = notificationSubject;
        this.notificationContent = notificationContent;
        this.notificationPostDate = notificationPostDate;
        this.notificationStatus = notificationStatus;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public MeetingTable getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(MeetingTable meetingId) {
        this.meetingId = meetingId;
    }

    public Member getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(Member memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getNotificationPostDate() {
        return notificationPostDate;
    }

    public void setNotificationPostDate(String notificationPostDate) {
        this.notificationPostDate = notificationPostDate;
    }

    public Integer getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Integer notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

}
