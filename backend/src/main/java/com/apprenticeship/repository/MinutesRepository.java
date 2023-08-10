package com.apprenticeship.repository;

import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.MinutesTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface is for accessing the database for minutes table
 */
@Repository("minutesJPARepository")
public interface MinutesRepository extends JpaRepository<MinutesTable, Integer> {

    boolean existsByMeetingId(MeetingTable meetingId);

    Optional<MinutesTable> findMinutesTableByMeetingId(MeetingTable meetingId);
}
