package com.apprenticeship.model;

import com.apprenticeship.enums.AttendeeRole;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * This class is used to create the Attendee table in the database.
 * Attendee table is a join table between the Member and meeting information tables.
 */
@Entity
@Table(
        name = "attendeeTable",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "attendee_table_id_unique",
                        columnNames = "attendeeTableId"
                )
        }
)
public class AttendeeTable {

    @Id
    @SequenceGenerator(
            name = "attendee_table_id_seq",
            sequenceName = "attendee_table_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attendee_table_id_seq"
    )
    private Integer attendeeTableId;

    @Column(
            nullable = false
    )
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member memberId;

    @Column(
            nullable = false
    )
    @ManyToOne
    @JoinColumn(name = "meetingId")
    private Meeting meetingId;

    private AttendeeRole attendeeRole;

    public AttendeeTable() {
    }

    public AttendeeTable(Integer attendeeTableId,
                         Member member,
                         Meeting meetingId,
                         AttendeeRole attendeeRole) {
        this.attendeeTableId = attendeeTableId;
        this.memberId = member;
        this.meetingId = meetingId;
        this.attendeeRole = attendeeRole;
    }

    public Integer getAttendeeTableId() {
        return attendeeTableId;
    }

    public void setAttendeeTableId(Integer attendeeTableId) {
        this.attendeeTableId = attendeeTableId;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public Meeting getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Meeting meetingId) {
        this.meetingId = meetingId;
    }

    public AttendeeRole getAttendeeRole() {
        return attendeeRole;
    }

    public void setAttendeeRole(AttendeeRole attendeeRole) {
        this.attendeeRole = attendeeRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendeeTable that = (AttendeeTable) o;
        return Objects.equals(attendeeTableId, that.attendeeTableId) && Objects.equals(memberId, that.memberId) && Objects.equals(meetingId, that.meetingId) && attendeeRole == that.attendeeRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendeeTableId, memberId, meetingId, attendeeRole);
    }

    @Override
    public String toString() {
        return "AttendeeTable{" +
               "attendeeTableId=" + attendeeTableId +
               ", memberId=" + memberId +
               ", meetingId=" + meetingId +
               ", attendeeRole=" + attendeeRole +
               '}';
    }
}
