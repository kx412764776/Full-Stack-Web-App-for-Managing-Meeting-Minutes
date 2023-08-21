package com.apprenticeship.service;

import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.MinutesTable;
import com.apprenticeship.repository.MeetingRepository;
import com.apprenticeship.repository.MinutesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is for manage minutes table
 */
@Service
public class MinutesService {

    private final MinutesRepository minutesRepository;
    private final MeetingRepository meetingRepository;

    public MinutesService(MinutesRepository minutesRepository,
                          MeetingRepository meetingRepository) {
        this.minutesRepository = minutesRepository;
        this.meetingRepository = meetingRepository;
    }

    /**
     * Method: Insert a new minute
     * @param meetingId, minutesContent
     */
    public void insertOrUpdateMinuteByMeetingId(Integer meetingId, String minutesContent) {
        // get meeting information from meeting id
        MeetingTable meetingTable = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with id [%s] not found".formatted(meetingId))
                ));

        // If the meeting id exists in the minutes table, update the minutes content
        if (minutesRepository.existsByMeetingId(meetingTable)) {
            // get minutes table from meeting id
            MinutesTable minutesTable = minutesRepository.findMinutesTableByMeetingId(meetingTable)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            ("minutes with meeting id [%s] not found".formatted(meetingId))
                    ));
            // update minutes content
            minutesTable.setMinutesContent(minutesContent);
            // save minutes table
            minutesRepository.save(minutesTable);
            return;
        }

        String meetingName = meetingTable.getMeetingName();

        // get meeting date from meeting id and transform to string
        String meetingDate = meetingTable.getMeetingDate().toString().split(" ")[0];

        // generate minute name
        String minutesFilename = meetingName + "_Minutes_" + meetingDate;

        MinutesTable minutesTable = new MinutesTable(meetingTable, minutesFilename, minutesContent);
        // insert new minute
        minutesRepository.save(minutesTable);
    }

    /**
     * Method: Get minutes information by meeting id
     * @param meetingId
     * @return minutes content
     */
    public String getMinutesByMeetingId(Integer meetingId) {
        // get meeting information from meeting id
        MeetingTable meetingTable = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with id [%s] not found".formatted(meetingId))
                ));

        // get minutes table from meeting id
        Optional<MinutesTable> minutesTable = minutesRepository.findMinutesTableByMeetingId(meetingTable);

        // return minutes content or if it isn't present, return null
        return minutesTable.map(MinutesTable::getMinutesContent).orElse(null);
    }
}
