package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.MeetingInfoDTOMapper;
import com.apprenticeship.dto.SignatureStatusDTO;
import com.apprenticeship.exception.DuplicateResourceException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.*;
import com.apprenticeship.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignatureServiceTest {

    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MinutesRepository minutesRepository;

    @Mock
    private AttendeeRepository attendeeRepository;

    @Mock
    private MeetingInfoDTOMapper meetingInfoDTOMapper;

    @InjectMocks
    private SignatureService signatureService;

    @Test
    void storeSignatureInfo_withValidInput_shouldSaveToDB() {
        // given
        int meetingId = 1;
        String memberEmail = "test@example.com";

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member = new Member();
        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.of(member));

        MinutesTable minutes = new MinutesTable();
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        // when
        signatureService.storeSignatureInfo(meetingId, memberEmail);

        // then
        verify(signatureRepository).save(any());
    }

    @Test
    void storeSignatureInfo_withInvalidMeetingId_shouldThrowException() {
        // given
        int meetingId = 1;
        String memberEmail = "test@example.com";

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member = new Member();
        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.of(member));

        MinutesTable minutes = new MinutesTable();
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        when(signatureRepository.existsByMemberIdAndMinutesId(any(), any()))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> signatureService.storeSignatureInfo(meetingId, memberEmail))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining(
                        "Member with email [%s] has already signed minutes with meeting id [%d]".formatted(
                                memberEmail, meetingId
                        )
                );


    }

    @Test
    void getSignatureStatus_whenSigned_shouldReturn1() {
        // given
        int meetingId = 1;
        String memberEmail = "test@example.com";

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member = new Member();
        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.of(member));

        MinutesTable minutes = new MinutesTable();
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        when(signatureRepository.existsByMemberIdAndMinutesId(any(), any()))
                .thenReturn(true);

        // when
        int result = signatureService.getSignatureStatus(meetingId, memberEmail);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void getSignatureStatus_whenNotSigned_shouldReturn0() {
        // given
        int meetingId = 1;
        String memberEmail = "test@example.com";

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member = new Member();
        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.of(member));

        MinutesTable minutes = new MinutesTable();
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        when(signatureRepository.existsByMemberIdAndMinutesId(any(), any()))
                .thenReturn(false);

        // when
        int result = signatureService.getSignatureStatus(meetingId, memberEmail);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void getSignatureStatus_whenMinutesNotFound_shouldThrowException() {
        // given
        int meetingId = 1;
        String memberEmail = "test@example.com";

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member = new Member();

        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> signatureService.getSignatureStatus(meetingId, memberEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Minutes with meeting id [%d] does not exist.".formatted(meetingId)
                );
    }

    @Test
    void getSignatureStatus_whenMemberNotFound_shouldThrowException() {
        // given
        int meetingId = 1;
        String memberEmail = "test@example.com";

        MeetingTable meeting = new MeetingTable();
        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        MinutesTable minutes = new MinutesTable();
        when(minutesRepository.findMinutesTableByMeetingId(meeting))
                .thenReturn(Optional.of(minutes));

        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> signatureService.getSignatureStatus(meetingId, memberEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Member with email [%s] does not exist.".formatted(memberEmail)
                );
    }

    @Test
    void checkAllAttendeeSigned_withSignedMeeting_shouldReturnMeetingInfoDTOList() {

        // given
        Member member1 = new Member();
        member1.setMemberId(1);

        Member member2 = new Member();
        member2.setMemberId(2);

        MeetingTable meeting = new MeetingTable();
        meeting.setMeetingId(1);

        Date meetingDate = new Date();
        MeetingInfoDTO meetingInfoDTO = new MeetingInfoDTO(
                1,
                "test meeting topic",
                "test meeting name",
                meetingDate,
                "1 hour",
                "test meeting description"
        );

        AttendeeTable attendee1 = new AttendeeTable();
        attendee1.setMemberId(member1);
        attendee1.setMeetingId(meeting);

        AttendeeTable attendee2 = new AttendeeTable();
        attendee2.setMemberId(member2);
        attendee2.setMeetingId(meeting);

        SignatureInfo signature1 = new SignatureInfo();
        signature1.setMemberId(member1);

        List<AttendeeTable> attendeeList = List.of(attendee1, attendee2);
        List<SignatureInfo> signatureList = List.of(signature1);

        when(meetingRepository.findAll()).thenReturn(List.of(meeting));
        when(attendeeRepository.findAllByMeetingId(any())).thenReturn(attendeeList);
        when(signatureRepository.findAllByMinutesId_MeetingId(any())).thenReturn(Optional.of(signatureList));
        when(meetingInfoDTOMapper.apply(any())).thenReturn(meetingInfoDTO);
        // when
        List<MeetingInfoDTO> result = signatureService.checkAllAttendeeSigned();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void checkAllAttendeeSigned_withNotFullSignedMeeting_shouldThrowException() {

        // given
        Member member1 = new Member();
        member1.setMemberId(1);

        Member member2 = new Member();
        member2.setMemberId(2);

        MeetingTable meeting = new MeetingTable();
        meeting.setMeetingId(1);

        Date meetingDate = new Date();
        MeetingInfoDTO meetingInfoDTO = new MeetingInfoDTO(
                1,
                "test meeting topic",
                "test meeting name",
                meetingDate,
                "1 hour",
                "test meeting description"
        );

        AttendeeTable attendee1 = new AttendeeTable();
        attendee1.setMemberId(member1);
        attendee1.setMeetingId(meeting);

        AttendeeTable attendee2 = new AttendeeTable();
        attendee2.setMemberId(member2);
        attendee2.setMeetingId(meeting);

        SignatureInfo signature1 = new SignatureInfo();
        signature1.setMemberId(member1);

        List<AttendeeTable> attendeeList = List.of(attendee1, attendee2);
        List<SignatureInfo> signatureList = List.of(signature1);

        when(meetingRepository.findAll()).thenReturn(List.of(meeting));
        when(attendeeRepository.findAllByMeetingId(any())).thenReturn(attendeeList);
        when(signatureRepository.findAllByMinutesId_MeetingId(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> signatureService.checkAllAttendeeSigned())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Signature with meeting id [%d] does not exist.".formatted(meeting.getMeetingId())
                );
    }

    @Test
    void isMinutesFullSigned_whenSigned_shouldReturnTrue()
            throws Exception {

        MeetingTable meeting = new MeetingTable(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        );

        Method method = SignatureService.class.getDeclaredMethod(
                "isMinutesFullSigned", MeetingTable.class);
        method.setAccessible(true);

        // given
        Member member1 = new Member();
        member1.setMemberId(1);

        Member member2 = new Member();
        member2.setMemberId(2);

        AttendeeTable attendee1 = new AttendeeTable();
        attendee1.setMemberId(member1);
        attendee1.setMeetingId(meeting);

        AttendeeTable attendee2 = new AttendeeTable();
        attendee2.setMemberId(member2);
        attendee2.setMeetingId(meeting);

        SignatureInfo signature1 = new SignatureInfo();
        signature1.setMemberId(member1);

        List<AttendeeTable> attendeeList = List.of(attendee1, attendee2);
        List<SignatureInfo> signatureList = List.of(signature1);

        when(attendeeRepository.findAllByMeetingId(any())).thenReturn(attendeeList);
        when(signatureRepository.findAllByMinutesId_MeetingId(any())).thenReturn(Optional.of(signatureList));

        // when
        boolean result = (boolean) method.invoke(signatureService, meeting);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void getNotAllAttendeeSigned_withUnsignedMeetings_shouldReturnMappedList() {

        // Given
        MeetingTable meeting1 = new MeetingTable(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        );
        MeetingTable meeting2 = new MeetingTable(
                2,
                "test meeting topic 2",
                "test meeting name 2",
                new Date(),
                "1 hour",
                "test meeting description 2"
        );

        Member member1 = new Member();
        member1.setMemberId(1);

        Member member2 = new Member();
        member2.setMemberId(2);

        AttendeeTable attendee1 = new AttendeeTable();
        attendee1.setMemberId(member1);
        attendee1.setMeetingId(meeting1);

        AttendeeTable attendee2 = new AttendeeTable();
        attendee2.setMemberId(member2);
        attendee2.setMeetingId(meeting2);

        SignatureInfo signature1 = new SignatureInfo();
        signature1.setMemberId(member1);

        List<AttendeeTable> attendeeList1 = List.of(attendee1);
        List<AttendeeTable> attendeeList2 = List.of(attendee2);
        List<SignatureInfo> signatureList1 = List.of(signature1);
        List<SignatureInfo> signatureList2 = List.of();

        when(meetingRepository.findAll()).thenReturn(List.of(meeting1, meeting2));
        when(attendeeRepository.findAllByMeetingId(meeting1)).thenReturn(attendeeList1);
        when(attendeeRepository.findAllByMeetingId(meeting2)).thenReturn(attendeeList2);

        when(signatureRepository.findAllByMinutesId_MeetingId(meeting1))
                .thenReturn(Optional.of(signatureList1));
        when(signatureRepository.findAllByMinutesId_MeetingId(meeting2))
                .thenReturn(Optional.of(signatureList2));

        // When
        List<MeetingInfoDTO> result = signatureService.getNotAllAttendeeSigned();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void getNotAllAttendeeSigned_withAllSignedMeetings_shouldReturnEmptyList() {

        // Given
        MeetingTable meeting1 = new MeetingTable(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        );
        MeetingTable meeting2 = new MeetingTable(
                2,
                "test meeting topic 2",
                "test meeting name 2",
                new Date(),
                "1 hour",
                "test meeting description 2"
        );

        Member member1 = new Member();
        member1.setMemberId(1);

        Member member2 = new Member();
        member2.setMemberId(2);

        AttendeeTable attendee1 = new AttendeeTable();
        attendee1.setMemberId(member1);
        attendee1.setMeetingId(meeting1);

        AttendeeTable attendee2 = new AttendeeTable();
        attendee2.setMemberId(member2);
        attendee2.setMeetingId(meeting2);

        SignatureInfo signature1 = new SignatureInfo();
        signature1.setMemberId(member1);

        SignatureInfo signature2 = new SignatureInfo();
        signature2.setMemberId(member2);

        List<AttendeeTable> attendeeList1 = List.of(attendee1);
        List<AttendeeTable> attendeeList2 = List.of(attendee2);
        List<SignatureInfo> signatureList1 = List.of(signature1);
        List<SignatureInfo> signatureList2 = List.of(signature2);

        when(meetingRepository.findAll()).thenReturn(List.of(meeting1, meeting2));
        when(attendeeRepository.findAllByMeetingId(meeting1)).thenReturn(attendeeList1);
        when(attendeeRepository.findAllByMeetingId(meeting2)).thenReturn(attendeeList2);
        when(signatureRepository.findAllByMinutesId_MeetingId(meeting1)).thenReturn(Optional.of(signatureList1));
        when(signatureRepository.findAllByMinutesId_MeetingId(meeting2)).thenReturn(Optional.of(signatureList2));

        // When
        List<MeetingInfoDTO> result = signatureService.getNotAllAttendeeSigned();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    @Test
    void getSignatureTableByMeetingId_withValidMeetingId_returnSignatureStatusDTOList() {

        // Given
        Integer meetingId = 1;
        MeetingTable meeting = new MeetingTable();

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        Member member1 = new Member(
                1,
                "Alex",
                "Smith",
                "alex@example.com",
                "password",
                "APPRENTICE"
        );
        Member member2 = new Member(
                2,
                "Connor",
                "Chen",
                "connor@example.com",
                "password",
                "APPRENTICE"
        );

        AttendeeTable attendee1 = new AttendeeTable(meeting, member1);
        AttendeeTable attendee2 = new AttendeeTable(meeting, member2);

        SignatureInfo signatureInfo = new SignatureInfo(
                1,
                new MinutesTable(meeting, "test minutes", "test minutes"),
                member1,
                1,
                new Date()
        );


        when(attendeeRepository.findAttendeeTableByMeetingId(meeting))
                .thenReturn(List.of(attendee1, attendee2));

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));

        when(signatureRepository.findByMinutesId_MeetingIdAndMemberId_MemberId(any(), eq(member1.getMemberId())))
                .thenReturn(signatureInfo);
        when(signatureRepository.findByMinutesId_MeetingIdAndMemberId_MemberId(any(), eq(member2.getMemberId())))
                .thenReturn(null);

        // When
        List<SignatureStatusDTO> result = signatureService.getSignatureTableByMeetingId(meetingId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).contains(
                new SignatureStatusDTO(
                        member1.getFirstName() + " " + member1.getLastName(),
                        member1.getEmail(),
                        member1.getMemberRole(),
                        1
                ),
                new SignatureStatusDTO(
                        member2.getFirstName() + " " + member2.getLastName(),
                        member2.getEmail(),
                        member2.getMemberRole(),
                        0
                )
        );
    }

    @Test
    void getSignatureTableByMeetingId_withNotExistsMeetingId_shouldThrowException() {

        // Given
        Integer meetingId = 1;

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> signatureService.getSignatureTableByMeetingId(meetingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Meeting with id [%d] does not exist.".formatted(meetingId)
                );
    }

    @Test
    void isAttendeeSigned_withValidInput_shouldReturnTrue() throws NoSuchMethodException {

        // Given
        Integer meetingId = 1;
        Integer memberId = 1;

        MeetingTable meeting = new MeetingTable();
        Member member = new Member();
        AttendeeTable attendee = new AttendeeTable(meeting, member);

        Method method = signatureService.getClass().getDeclaredMethod(
                "isAttendeeSigned", Integer.class, Integer.class);
        method.setAccessible(true);

        when(meetingRepository.findMeetingTableByMeetingId(meetingId))
                .thenReturn(Optional.of(meeting));
        when(signatureRepository.findByMinutesId_MeetingIdAndMemberId_MemberId(meeting, memberId))
                .thenReturn(new SignatureInfo());

        // When
        boolean result;
        try {
            result = (boolean) method.invoke(signatureService, meetingId, memberId);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void getSignedMeetingListByMemberEmail_withValidInput_shouldReturnMeetingInfoDTOList() {

        // Given
        String memberEmail = "test@example.com";
        Member member = new Member();
        member.setMemberId(1);

        MeetingTable meeting1 = new MeetingTable(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        );

        MeetingTable meeting2 = new MeetingTable(
                2,
                "test meeting topic 2",
                "test meeting name 2",
                new Date(),
                "1 hour",
                "test meeting description 2"
        );

        AttendeeTable attendee1 = new AttendeeTable(meeting1, member);
        AttendeeTable attendee2 = new AttendeeTable(meeting2, member);

        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.of(member));

        when(attendeeRepository.findAllByMemberId(member))
                .thenReturn(List.of(attendee1, attendee2));


        when(signatureRepository.existsByMemberIdAndMinutesId_MeetingId(member, meeting1))
                .thenReturn(true);
        when(signatureRepository.existsByMemberIdAndMinutesId_MeetingId(member, meeting2))
                .thenReturn(true);

        MeetingInfoDTO meetingDTO1 = new MeetingInfoDTO(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        );
        MeetingInfoDTO meetingDTO2 = new MeetingInfoDTO(
                2,
                "test meeting topic 2",
                "test meeting name 2",
                new Date(),
                "1 hour",
                "test meeting description 2"
        );

        when(meetingInfoDTOMapper.apply(meeting1)).thenReturn(meetingDTO1);
        when(meetingInfoDTOMapper.apply(meeting2)).thenReturn(meetingDTO2);

        // When
        List<MeetingInfoDTO> result = signatureService.getSignedMeetingListByMemberEmail(memberEmail);

        // Then
        assertThat(result)
                .containsExactlyInAnyOrder(meetingDTO1, meetingDTO2);

    }

    @Test
    void getSignedMeetingListByMemberEmail_withInvalidMemberEmail_shouldThrowException() {

        // Given
        String memberEmail = "test@example.com";

        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> signatureService.getSignedMeetingListByMemberEmail(memberEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Member with email [%s] does not exist.".formatted(memberEmail)
                );
    }

    @Test
    void getNotSignedMeetingListByMemberEmail_withValidInput_shouldReturnMeetingInfoDTOList() {

        // Given
        String memberEmail = "test@example.com";
        Member member = new Member();
        member.setMemberId(1);

        MeetingTable meeting1 = new MeetingTable(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        );

        MeetingTable meeting2 = new MeetingTable(
                2,
                "test meeting topic 2",
                "test meeting name 2",
                new Date(),
                "1 hour",
                "test meeting description 2"
        );

        AttendeeTable attendee1 = new AttendeeTable(meeting1, member);
        AttendeeTable attendee2 = new AttendeeTable(meeting2, member);

        when(memberRepository.findMemberByEmail(memberEmail))
                .thenReturn(Optional.of(member));

        when(attendeeRepository.findAllByMemberId(member))
                .thenReturn(List.of(attendee1, attendee2));


        when(signatureRepository.existsByMemberIdAndMinutesId_MeetingId(member, meeting1))
                .thenReturn(true);
        when(signatureRepository.existsByMemberIdAndMinutesId_MeetingId(member, meeting2))
                .thenReturn(false);

        MeetingInfoDTO meetingDTO = new MeetingInfoDTO(
                2,
                "test meeting topic 2",
                "test meeting name 2",
                new Date(),
                "1 hour",
                "test meeting description 2"
        );

        when(meetingInfoDTOMapper.apply(meeting2)).thenReturn(meetingDTO);

        // When
        List<MeetingInfoDTO> result = signatureService.getNotSignedMeetingListByMemberEmail(memberEmail);

        // Then
        assertThat(result)
                .containsExactlyInAnyOrder(meetingDTO);
    }
}