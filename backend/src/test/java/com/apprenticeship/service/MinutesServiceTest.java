package com.apprenticeship.service;

import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.MinutesTable;
import com.apprenticeship.repository.MeetingRepository;
import com.apprenticeship.repository.MinutesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinutesServiceTest {

    @Mock
    MinutesRepository minutesRepository;

    @Mock
    MeetingRepository meetingRepository;

    @InjectMocks
    MinutesService minutesService;

    @Test
    void insertOrUpdateMinuteByMeetingId_newMinute_shouldSaveToDb() {
        // given
        int meetingId = 1;
        String minutesContent = "test content";

        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "test meeting",
                "test meeting name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        when(minutesRepository.existsByMeetingId(meeting)).thenReturn(false);

        // when
        minutesService.insertOrUpdateMinuteByMeetingId(meetingId, minutesContent);

        // then
        verify(minutesRepository).save(any());
    }

    @Test
    void insertOrUpdateMinuteByMeetingId_withNotExistsMeetingId_shouldThrowException() {
        // given
        int meetingId = 1;
        String minutesContent = "test content";

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> minutesService.insertOrUpdateMinuteByMeetingId(meetingId, minutesContent))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("meeting with id [%s] not found".formatted(meetingId));
    }

    @Test
    void insertOrUpdateMinuteByMeetingId_withNotExistsMinutes_shouldThrowException() {
        // given
        int meetingId = 1;
        String minutesContent = "test content";

        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "test meeting topic",
                "test meeting name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        when(minutesRepository.existsByMeetingId(meeting)).thenReturn(true);
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> minutesService.insertOrUpdateMinuteByMeetingId(meetingId, minutesContent))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("minutes with meeting id [%s] not found".formatted(meetingId));
    }

    @Test
    void insertOrUpdateMinuteByMeetingId_existingMinute_shouldUpdateDb() {
        // given
        int meetingId = 1;
        String updatedContent = "updated content";

        MeetingTable meeting = new MeetingTable();
        MinutesTable existingMinute = new MinutesTable();

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        when(minutesRepository.existsByMeetingId(meeting)).thenReturn(true);
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(existingMinute));

        // when
        minutesService.insertOrUpdateMinuteByMeetingId(meetingId, updatedContent);

        // then
        verify(minutesRepository).save(existingMinute);
        assertThat(existingMinute.getMinutesContent()).isEqualTo(updatedContent);
    }

    @Test
    void getMinutesByMeetingId_withValidId_shouldReturnContent() {
        // given
        int meetingId = 1;
        String expectedContent = "test content";

        MeetingTable meeting = new MeetingTable();
        MinutesTable minutes = new MinutesTable();
        minutes.setMinutesContent(expectedContent);

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        // when
        String result = minutesService.getMinutesByMeetingId(meetingId);

        // then
        assertThat(result).isEqualTo(expectedContent);
    }

    @Test
    void getMinutesByMeetingId_withNotExistsMeetingId_shouldThrowException() {
        // given
        int meetingId = 1;

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> minutesService.getMinutesByMeetingId(meetingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("meeting with id [%s] not found".formatted(meetingId));
    }

}