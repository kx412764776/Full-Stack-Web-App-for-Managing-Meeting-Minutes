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
public class MeetingTable {

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

    public MeetingTable() {
    }

    public MeetingTable(Integer meetingId,
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
        MeetingTable meetingTable = (MeetingTable) o;
        return Objects.equals(meetingId, meetingTable.meetingId) && Objects.equals(meetingTopic, meetingTable.meetingTopic) && Objects.equals(meetingName, meetingTable.meetingName) && Objects.equals(meetingDate, meetingTable.meetingDate) && Objects.equals(meetingDuration, meetingTable.meetingDuration) && Objects.equals(meetingDescription, meetingTable.meetingDescription);
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
