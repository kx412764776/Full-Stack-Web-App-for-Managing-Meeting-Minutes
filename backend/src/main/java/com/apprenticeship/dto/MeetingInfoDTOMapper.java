package com.apprenticeship.dto;

import com.apprenticeship.model.MeetingTable;
import org.springframework.stereotype.Service;

import java.util.function.Function;
/**
 * This class is responsible for mapping MeetingInfoDTO to get meetings information by member email.
 */
@Service
public class MeetingInfoDTOMapper implements Function<MeetingTable, MeetingInfoDTO> {

    @Override
    public MeetingInfoDTO apply(MeetingTable meetingTable) {
        return new MeetingInfoDTO(
                meetingTable.getMeetingId(),
                meetingTable.getMeetingTopic(),
                meetingTable.getMeetingName(),
                meetingTable.getMeetingDate(),
                meetingTable.getMeetingDuration(),
                meetingTable.getMeetingDescription()
        );
    }
}
