package com.apprenticeship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AttendeeTableTest {

    private AttendeeTable attendeeTable;
    private Member member;
    private MeetingTable meetingTable;

    @BeforeEach
    void setUp() {
        member = new Member(
                1,
                "Qizhao",
                "Chen",
                "qizhao@example.com",
                "123456",
                "APPRENTICE"
        );
        meetingTable = new MeetingTable(
                1,
                "apprenticeship",
                "apprenticeship",
                new Date(),
                "1 hour",
                "java and react project"
        );
        attendeeTable = new AttendeeTable(meetingTable, member);
    }

    @Test
    void testConstructorAndGetter() {
        assertThat(attendeeTable).isNotNull();
        assertThat(attendeeTable.getMeetingId()).isEqualTo(meetingTable);
        assertThat(attendeeTable.getMemberId()).isEqualTo(member);

        AttendeeTable attendeeTable2 = new AttendeeTable();
        attendeeTable2.setMeetingId(meetingTable);
        attendeeTable2.setMemberId(member);
        assertThat(attendeeTable2).isNotNull();
        assertThat(attendeeTable2.getMeetingId()).isEqualTo(meetingTable);
        assertThat(attendeeTable2.getMemberId()).isEqualTo(member);

    }

    @Test
    void testEqualsAndHashCode() {
        AttendeeTable attendeeTable2 = new AttendeeTable(meetingTable, member);
        assertThat(attendeeTable).isEqualTo(attendeeTable2);
        assertThat(attendeeTable.hashCode()).isEqualTo(attendeeTable2.hashCode());
    }

    @Test
    void testToString() {
        assertThat(attendeeTable.toString()).isEqualTo("AttendeeTable{" +
                "attendeeTableId=" + null +
                ", memberId=" + member +
                ", meetingId=" + meetingTable +
                '}');
    }
}