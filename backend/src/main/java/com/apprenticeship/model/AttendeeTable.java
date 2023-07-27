package com.apprenticeship.model;

import com.apprenticeship.enums.MemberRole;
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

    // foreign key to member table (memberId)
    @ManyToOne
    @JoinColumn(
            name = "memberId",
            referencedColumnName = "memberId",
            foreignKey = @ForeignKey(
                    name = "member_id_fk"
            ),
            nullable = false
    )
    private Member memberId;

    // foreign key to meeting table (meetingId)
    @ManyToOne
    @JoinColumn(
            name = "meetingId",
            referencedColumnName = "meetingId",
            foreignKey = @ForeignKey(
                    name = "meeting_id_fk"
            ),
            nullable = false
    )
    private MeetingTable meetingId;


    public AttendeeTable() {
    }

    public AttendeeTable(Integer attendeeTableId,
                         Member memberId,
                         MeetingTable meetingId
    ) {
        this.attendeeTableId = attendeeTableId;
        this.memberId = memberId;
        this.meetingId = meetingId;
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

    public MeetingTable getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(MeetingTable meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendeeTable that = (AttendeeTable) o;
        return Objects.equals(attendeeTableId, that.attendeeTableId) && Objects.equals(memberId, that.memberId) && Objects.equals(meetingId, that.meetingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendeeTableId, memberId, meetingId);
    }

    @Override
    public String toString() {
        return "AttendeeTable{" +
               "attendeeTableId=" + attendeeTableId +
               ", memberId=" + memberId +
               ", meetingId=" + meetingId +
               '}';
    }
}
