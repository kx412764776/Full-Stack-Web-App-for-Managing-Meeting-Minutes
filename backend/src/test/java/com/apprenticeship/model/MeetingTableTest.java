package com.apprenticeship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MeetingTableTest {

    private MeetingTable meetingTable;

    @BeforeEach
    void setUp() {
        meetingTable = new MeetingTable();
    }

    @Test
    void testConstructorAndGetter() {
        meetingTable = new MeetingTable(
                1,
                "apprenticeship", // meetingTopic
                "apprenticeship", // meetingName
                new Date(), // meetingDate
                "1 hour", // meetingDuration
                "java and react project" // meetingDescription
        );

        assertThat(meetingTable).isNotNull();
        assertThat(meetingTable.getMeetingTopic()).isEqualTo("apprenticeship");
        assertThat(meetingTable.getMeetingName()).isEqualTo("apprenticeship");
        assertThat(meetingTable.getMeetingDate()).isNotNull();
        assertThat(meetingTable.getMeetingDuration()).isEqualTo("1 hour");
        assertThat(meetingTable.getMeetingDescription()).isEqualTo("java and react project");

        MeetingTable meetingTable2 = new MeetingTable(
                "apprenticeship", // meetingTopic
                "apprenticeship", // meetingName
                new Date(), // meetingDate
                "1 hour", // meetingDuration
                "java and react project" // meetingDescription
        );

        assertThat(meetingTable2).isNotNull();
        assertThat(meetingTable2.getMeetingTopic()).isEqualTo("apprenticeship");
        assertThat(meetingTable2.getMeetingName()).isEqualTo("apprenticeship");
        assertThat(meetingTable2.getMeetingDate()).isNotNull();
        assertThat(meetingTable2.getMeetingDuration()).isEqualTo("1 hour");
        assertThat(meetingTable2.getMeetingDescription()).isEqualTo("java and react project");

    }

    @Test
    void testSetters() {
        meetingTable.setMeetingId(1);
        meetingTable.setMeetingTopic("manage meeting minutes");
        meetingTable.setMeetingName("manage meeting minutes");
        meetingTable.setMeetingDate(new Date());
        meetingTable.setMeetingDuration("2 hour");
        meetingTable.setMeetingDescription("springboot and react project");

        assertThat(meetingTable).isNotNull();
        assertThat(meetingTable.getMeetingId()).isEqualTo(1);
        assertThat(meetingTable.getMeetingTopic()).isEqualTo("manage meeting minutes");
        assertThat(meetingTable.getMeetingName()).isEqualTo("manage meeting minutes");
        assertThat(meetingTable.getMeetingDate()).isNotNull();
        assertThat(meetingTable.getMeetingDuration()).isEqualTo("2 hour");
        assertThat(meetingTable.getMeetingDescription()).isEqualTo("springboot and react project");

    }



}