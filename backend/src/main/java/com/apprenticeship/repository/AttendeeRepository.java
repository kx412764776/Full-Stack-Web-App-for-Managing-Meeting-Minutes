package com.apprenticeship.repository;

import com.apprenticeship.model.AttendeeTable;
import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface is for accessing the database for attendee table
 */
@Repository("attendeeJPARepository")
public interface AttendeeRepository extends JpaRepository<AttendeeTable, Integer> {


    List<AttendeeTable> findAllByMemberId(Member member);

    boolean existsByMemberIdAndMeetingId(Member member, MeetingTable meetingTable);

    // get all attendees by meeting id
    List<AttendeeTable> findAllByMeetingId(MeetingTable meetingTable);

    void deleteDistinctByMemberIdAndMeetingId(Member member, MeetingTable meetingTableInfo);

    // get all attendees by meeting id
    List<AttendeeTable> findAttendeeTableByMeetingId(MeetingTable meetingTable);

}
