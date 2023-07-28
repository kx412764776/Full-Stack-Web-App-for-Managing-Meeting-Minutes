package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is for implementing the business logic of the MeetingController
 *
 */
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public MeetingService(@Qualifier("meetingJPARepository") MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // TODO: According to the email in the member table to find the corresponding ID,
    //  and according to the ID from the attendee table to find the corresponding meetingID,
    //  and then according to the meetingID from the meeting table to find the corresponding meeting
    public List<MeetingInfoDTO> getMeetingInfoByMemberEmail(String memberEmail) {
        return null;
    }
}
