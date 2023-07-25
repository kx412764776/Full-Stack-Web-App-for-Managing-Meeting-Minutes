package com.apprenticeship.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

/**
 * This class is a model for the Meeting table in the database.
 */
@Entity
@Table(
        name = "meetingTable",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "meeting_id_unique",
                        columnNames = "meetingId"
                )
        }
)
public class Meeting {

    @Id
    @SequenceGenerator(
            name = "meeting_id_seq",
            sequenceName = "meeting_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "meeting_id_seq"
    )
    private Integer meetingId;

    @Column(
            nullable = false
    )
    private String meetingTopic;

    @Column(
            nullable = false
    )
    private String meetingName;

    @Column(
            nullable = false
    )
    private Date meetingDate;

    @Column(
            nullable = false
    )
    private String meetingDuration;

    private String meetingDescription;

    public Meeting() {
    }

    public Meeting(Integer meetingId,
                   String meetingTopic,
                   String meetingName,
                   Date meetingDate,
                   String meetingDuration,
                   String meetingDescription) {
        this.meetingId = meetingId;
        this.meetingTopic = meetingTopic;
        this.meetingName = meetingName;
        this.meetingDate = meetingDate;
        this.meetingDuration = meetingDuration;
        this.meetingDescription = meetingDescription;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingTopic() {
        return meetingTopic;
    }

    public void setMeetingTopic(String meetingTopic) {
        this.meetingTopic = meetingTopic;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(String meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public void setMeetingDescription(String meetingDescription) {
        this.meetingDescription = meetingDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(meetingId, meeting.meetingId) && Objects.equals(meetingTopic, meeting.meetingTopic) && Objects.equals(meetingName, meeting.meetingName) && Objects.equals(meetingDate, meeting.meetingDate) && Objects.equals(meetingDuration, meeting.meetingDuration) && Objects.equals(meetingDescription, meeting.meetingDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, meetingTopic, meetingName, meetingDate, meetingDuration, meetingDescription);
    }

    @Override
    public String toString() {
        return "Meeting{" +
               "meetingId=" + meetingId +
               ", meetingTopic='" + meetingTopic + '\'' +
               ", meetingName='" + meetingName + '\'' +
               ", meetingDate=" + meetingDate +
               ", meetingDuration='" + meetingDuration + '\'' +
               ", meetingDescription='" + meetingDescription + '\'' +
               '}';
    }
}
