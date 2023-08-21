package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.MeetingInfoDTOMapper;
import com.apprenticeship.exception.RequestException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.*;
import com.apprenticeship.repository.*;
import com.apprenticeship.requestsAndResponses.MeetingUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AttendeeRepository attendeeRepository;

    @Mock
    private MinutesRepository minutesRepository;

    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private MeetingInfoDTOMapper meetingInfoDTOMapper;

    @InjectMocks
    private MeetingService meetingService;

    @Test
    void getMeetingInfoByMemberEmail_withValidEmail_shouldReturnList() {
        // given
        String email = "test@email.com";
        Member member = new Member();
        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(member));

        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(attendeeRepository.findAllByMemberId(member))
                .thenReturn(List.of(new AttendeeTable(meeting, member)));

        when(meetingInfoDTOMapper.apply(meeting))
                .thenReturn(new MeetingInfoDTO(
                        1,
                        "test topic",
                        "test name",
                        meetingDate,
                        "1 hour",
                        "test description"
                ));

        // when
        List<MeetingInfoDTO> result = meetingService.getMeetingInfoByMemberEmail(email);

        // then
        verify(memberRepository).findMemberByEmail(email);
        verify(meetingInfoDTOMapper).apply(any());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).meetingId()).isEqualTo(1);
        assertThat(result.get(0).meetingTopic()).isEqualTo("test topic");
        assertThat(result.get(0).meetingName()).isEqualTo("test name");
        assertThat(result.get(0).meetingDate()).isEqualTo(meetingDate);
        assertThat(result.get(0).meetingDuration()).isEqualTo("1 hour");
        assertThat(result.get(0).meetingDescription()).isEqualTo("test description");
    }

    @Test
    void getMeetingInfoByMemberEmail_withInvalidEmail_shouldThrowException() {
        // given
        String invalidEmail = "invalid-email";
        when(memberRepository.findMemberByEmail(invalidEmail)).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.getMeetingInfoByMemberEmail(invalidEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("member with email [%s] not found".formatted(invalidEmail));
    }

    @Test
    void insertMeetingInfo_withValidData_shouldSaveToDb() {
        // given
        MeetingTable meeting = new MeetingTable();

        // when
        meetingService.insertMeetingInfo(meeting);

        // then
        verify(meetingRepository).save(meeting);
    }

    @Test
    void insertAttendeeInfo_withNewAttendees_shouldSaveToDb() {
        // given
        List<String> emails = List.of("test1@example.com", "test2@example.com");
        Member member1 = new Member();
        Member member2 = new Member();
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.of(member1), Optional.of(member2));
        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(anyInt())).thenReturn(Optional.of(meeting));

        // when
        meetingService.insertAttendeeInfo(emails, 1);

        // then
        verify(memberRepository, times(2)).findMemberByEmail(any());
        verify(attendeeRepository, times(2)).save(any());
    }

    @Test
    void insertAttendeeInfo_withInvalidEmail_shouldThrowException() {
        // given
        List<String> emails = List.of("invalid-email");
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.insertAttendeeInfo(emails, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("member with email [%s] not found".formatted(Optional.empty()));
    }

    @Test
    void insertAttendeeInfo_withInvalidMeetingId_shouldThrowException() {
        // given
        List<String> emails = List.of("test@example.com");
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.of(new Member()));
        when(meetingRepository.findMeetingTableByMeetingId(anyInt())).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.insertAttendeeInfo(emails, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(1));
    }

    @Test
    void insertAttendeeInfo_withExistingAttendees_shouldThrowException() {
        // given
        List<String> emails = List.of("exist-attendee@example.com");
        Member member = new Member(
                1,
                "Alex",
                "Smith",
                "exist-attendee@example.com",
                "password",
                "APPRENTICE"
        );
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.of(member));

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(anyInt())).thenReturn(Optional.of(meeting));

        when(attendeeRepository.existsByMemberIdAndMeetingId(member, meeting)).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> meetingService.insertAttendeeInfo(emails, 1))
                .isInstanceOf(RequestException.class)
                .hasMessage("member with email [%s] already exists in meeting with meetingId [%s]"
                        .formatted(emails.get(0), 1));
    }

    @Test
    void getAllMeetingInfo_shouldReturnMappedList() {

        // given
        List<MeetingTable> meetings = List.of(new MeetingTable(), new MeetingTable());
        when(meetingRepository.findAll()).thenReturn(meetings);
        when(meetingRepository.findAll()).thenReturn(meetings);

        // when
        List<MeetingInfoDTO> result = meetingService.getAllMeetingInfo();

        // then
        verify(meetingRepository).findAll();
        verify(meetingInfoDTOMapper, times(meetings.size())).apply(any());
        assertThat(meetings.size()).isEqualTo(result.size());
    }

    @Test
    void getAttendeeInfoByMeetingId_withValidId_shouldReturnEmailList() {

        // given
        Integer meetingId = 1;
        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member1 = new Member(
                meetingId,
                "Alex",
                "Smith",
                "test-alex@example.com",
                "password",
                "APPRENTICE"
        );

        Member member2 = new Member(
                meetingId,
                "Connor",
                "Chen",
                "test-connor@example.com",
                "password",
                "APPRENTICE"
        );

        AttendeeTable attendee1 = new AttendeeTable(
                meeting,
                member1
        );

        AttendeeTable attendee2 = new AttendeeTable(
                meeting,
                member2
        );

        when(attendeeRepository.findAllByMeetingId(meeting)).thenReturn(
                List.of(
                        new AttendeeTable(meeting, member1),
                        new AttendeeTable(meeting, member2)
                ));

        // when
        List<String> result = meetingService.getAttendeeInfoByMeetingId(meetingId);

        // then
        verify(meetingRepository).findMeetingTableByMeetingId(meetingId);
        verify(attendeeRepository).findAllByMeetingId(meeting);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo("test-alex@example.com,Alex Smith(APPRENTICE)");
        assertThat(result.get(1)).isEqualTo("test-connor@example.com,Connor Chen(APPRENTICE)");
    }

    @Test
    void getAttendeeInfoByMeetingId_withInvalidId_shouldThrowException() {
        // given
        Integer invalidId = -1;
        when(meetingRepository.findMeetingTableByMeetingId(invalidId))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.getAttendeeInfoByMeetingId(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(invalidId));
    }

    @Test
    void removeAttendeeFromAttendeeTable_withValidInput_shouldDeleteEntries() {

        // given
        Integer meetingId = 1;
        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member = new Member();
        when(memberRepository.findMemberByEmail(any()))
                .thenReturn(Optional.of(member));

        when(attendeeRepository.existsByMemberIdAndMeetingId(member, meeting))
                .thenReturn(true);

        // when
        meetingService.removeAttendeeFromAttendeeTable(meetingId, List.of("test@email.com"));

        // then
        verify(meetingRepository).findMeetingTableByMeetingId(meetingId);
        verify(memberRepository).findMemberByEmail(any());
        verify(attendeeRepository).deleteDistinctByMemberIdAndMeetingId(member, meeting);
    }

    @Test
    void removeAttendeeFromAttendeeTable_withInvalidEmail_shouldThrowException() {
        // given
        String invalidEmail = "invalid-email";
        when(memberRepository.findMemberByEmail(invalidEmail))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() ->
                meetingService.removeAttendeeFromAttendeeTable(
                        1, List.of(invalidEmail)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("member with email [%s] not found".formatted(Optional.empty()));
    }

    @Test
    void removeAttendeeFromAttendeeTable_withInvalidMeetingId_shouldThrowException() {
        // given
        String validEmail = "test@example.com";

        when(memberRepository.findMemberByEmail(validEmail))
                .thenReturn(Optional.of(new Member()));

        Integer invalidMeetingId = -1;
        when(meetingRepository.findMeetingTableByMeetingId(invalidMeetingId))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() ->
                meetingService.removeAttendeeFromAttendeeTable(
                        invalidMeetingId, List.of("test@example.com")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(invalidMeetingId));
    }

    @Test
    void removeAttendeeFromAttendeeTable_withInvalidAttendee_shouldThrowException() {
        // given
        String email = "test@example.com";
        Member member = new Member(
                1,
                "Alex",
                "Smith",
                email,
                "password",
                "APPRENTICE"
        );
        when(memberRepository.findMemberByEmail(email))
                .thenReturn(Optional.of(member));

        Integer validMeetingId = 1;
        MeetingTable meeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                new Date(),
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(validMeetingId))
                .thenReturn(Optional.of(meeting)
        );

        when(attendeeRepository.existsByMemberIdAndMeetingId(member, meeting))
                .thenReturn(false);

        // when && then
        assertThatThrownBy(() ->
                meetingService.removeAttendeeFromAttendeeTable(
                        validMeetingId, List.of(email)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("member with email [%s] does not exist in meeting with meetingId [%s]"
                        .formatted(email, validMeetingId));
    }

    @Test
    void getMeetingInfoByMeetingId_withValidId_shouldReturnMeetingInfoDTO() {

        // given
        Integer meetingId = 1;
        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        when(meetingInfoDTOMapper.apply(meeting))
                .thenReturn(new MeetingInfoDTO(
                        1,
                        "test topic",
                        "test name",
                        meetingDate,
                        "1 hour",
                        "test description"
                ));

        // when
        MeetingInfoDTO result = meetingService.getMeetingInfoByMeetingId(meetingId);

        // then
        verify(meetingRepository).findMeetingTableByMeetingId(meetingId);
        verify(meetingInfoDTOMapper).apply(meeting);
        assertThat(result.meetingId()).isEqualTo(1);
        assertThat(result.meetingTopic()).isEqualTo("test topic");
        assertThat(result.meetingName()).isEqualTo("test name");
        assertThat(result.meetingDate()).isEqualTo(meetingDate);
        assertThat(result.meetingDuration()).isEqualTo("1 hour");
        assertThat(result.meetingDescription()).isEqualTo("test description");
    }

    @Test
    void getMeetingInfoById_withValidId_shouldReturnMeetingInfo() {

        // given
        Integer meetingId = 1;
        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        // when
        MeetingTable result = meetingService.getMeetingInfoById(meetingId);

        // then
        verify(meetingRepository).findMeetingTableByMeetingId(meetingId);
        assertThat(result.getMeetingId()).isEqualTo(1);
        assertThat(result.getMeetingTopic()).isEqualTo("test topic");
        assertThat(result.getMeetingName()).isEqualTo("test name");
        assertThat(result.getMeetingDate()).isEqualTo(meetingDate);
        assertThat(result.getMeetingDuration()).isEqualTo("1 hour");
        assertThat(result.getMeetingDescription()).isEqualTo("test description");
    }

    @Test
    void getMeetingInfoByMeetingId_withInvalidId_shouldThrowException() {

        // given
        Integer invalidId = -1;
        when(meetingRepository.findMeetingTableByMeetingId(invalidId))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.getMeetingInfoByMeetingId(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(invalidId));
    }

    @Test
    void getMeetingInfoById_withInvalidId_shouldThrowException() {

        // given
        Integer invalidId = -1;
        when(meetingRepository.findMeetingTableByMeetingId(invalidId))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.getMeetingInfoById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(invalidId));
    }

    @Test
    void editMeetingInfoByMeetingId_withNoChanges_shouldThrowException() {

        // given
        Integer meetingId = 1;
        Date meetingDate = new Date();
        MeetingTable existingMeeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(existingMeeting));


        MeetingUpdateRequest updateRequest = new MeetingUpdateRequest(
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );

        // when && then
        assertThatThrownBy(() -> meetingService.editMeetingInfoByMeetingId(meetingId, updateRequest))
                .isInstanceOf(RequestException.class)
                .hasMessage("Meeting information is not changed with meetingId [%s]".formatted(meetingId));
    }

    @Test
    void editMeetingInfoByMeetingId_withValidUpdate_shouldUpdateMeeting() {
        // given
        Integer meetingId = 1;
        Date meetingDate = new Date();

        MeetingTable existingMeeting = new MeetingTable(
                1,
                "test topic",
                "test name",
                meetingDate,
                "1 hour",
                "test description"
        );

        Date newMeetingDate = new Date();
        MeetingUpdateRequest updateRequest = new MeetingUpdateRequest(
                "updated topic",
                "updated name",
                newMeetingDate,
                "1 hour(updated)",
                "updated description"
        );


        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(existingMeeting));

        // when
        meetingService.editMeetingInfoByMeetingId(meetingId, updateRequest);

        // then
        verify(meetingRepository).findMeetingTableByMeetingId(meetingId);
        verify(meetingRepository).save(existingMeeting);
    }

    @Test
    void editMeetingInfoById_withNotExistsMeetingId_shouldThrowException() {
        // given
        Integer invalidId = -1;
        Date meetingDate = new Date();

        MeetingUpdateRequest updateRequest = new MeetingUpdateRequest(
                "updated topic",
                "updated name",
                meetingDate,
                "1 hour(updated)",
                "updated description"
        );

        when(meetingRepository.findMeetingTableByMeetingId(invalidId))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.editMeetingInfoByMeetingId(invalidId, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(invalidId));
    }

    @Test
    void deleteMeetingInfoByMeetingId_withValidId_shouldDeleteRelatedData() {
        // given
        Integer meetingId = 1;
        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        AttendeeTable attendee = new AttendeeTable();
        List<AttendeeTable> attendees = List.of(attendee);
        when(attendeeRepository.findAttendeeTableByMeetingId(meeting))
                .thenReturn(attendees);

        MinutesTable minutes = new MinutesTable();
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        SignatureInfo signature = new SignatureInfo();
        List<SignatureInfo> signatures = List.of(signature);
        when(signatureRepository.findAllByMinutesId_MeetingId(meeting))
                .thenReturn(Optional.of(signatures));

        // when
        meetingService.deleteMeetingInfoByMeetingId(meetingId);

        // then
        verify(meetingRepository).delete(meeting);
        verify(attendeeRepository).deleteAll(anyList());
        verify(minutesRepository).delete(any());
        verify(signatureRepository).deleteAll(anyList());
    }

    @Test
    void deleteMeetingInfoById_withInvalidId_shouldThrowException() {
        // given
        Integer invalidId = -1;
        when(meetingRepository.findMeetingTableByMeetingId(invalidId))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> meetingService.deleteMeetingInfoByMeetingId(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("meeting with meetingId [%s] not found".formatted(invalidId));
    }
}