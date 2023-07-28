package com.apprenticeship.repository;

import com.apprenticeship.model.MeetingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository("meetingJPARepository")
public interface MeetingRepository extends JpaRepository<MeetingTable, Integer> {

}
