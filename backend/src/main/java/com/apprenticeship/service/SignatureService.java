package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.MeetingInfoDTOMapper;
import com.apprenticeship.dto.SignatureStatusDTO;
import com.apprenticeship.enums.MemberRole;
import com.apprenticeship.exception.DuplicateResourceException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.*;
import com.apprenticeship.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is to handle signatureInfo related requests.
 */
@Service
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MinutesRepository minutesRepository;
    private final AttendeeRepository attendeeRepository;
    private final MeetingInfoDTOMapper meetingInfoDTOMapper;

    public SignatureService(SignatureRepository signatureRepository,
                            MeetingRepository meetingRepository,
                            MemberRepository memberRepository,
                            MinutesRepository minutesRepository,
                            AttendeeRepository attendeeRepository,
                            MeetingInfoDTOMapper meetingInfoDTOMapper) {
        this.signatureRepository = signatureRepository;
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.minutesRepository = minutesRepository;
        this.attendeeRepository = attendeeRepository;
        this.meetingInfoDTOMapper = meetingInfoDTOMapper;
    }

    /**
     * store signature information according to meeting id and member email
     */
    public void storeSignatureInfo(Integer meetingId, String memberEmail) {

        // get minutes object from meeting id
        MinutesTable minutesTable = getMinutesTableFromMeetingId(meetingId);

        // get member object from member email
        Member member = getMemberFromMemberEmail(memberEmail);

        // check if the member has already signed
        if (signatureRepository.existsByMemberIdAndMinutesId(member, minutesTable)) {
            throw new DuplicateResourceException(
                    "Member with email [%s] has already signed minutes with meeting id [%d]".formatted(
                            memberEmail,
                            meetingId
                    )
            );
        }

        // store signature information
        SignatureInfo signatureInfo = new SignatureInfo(
                minutesTable,
                member,
                1,
                new Date()
        );
        signatureRepository.save(signatureInfo);

    }

    public Integer getSignatureStatus(Integer meetingId, String memberEmail) {
        MinutesTable minutesTable = getMinutesTableFromMeetingId(meetingId);
        Member member = getMemberFromMemberEmail(memberEmail);

        // check if member has signed the minutes of related meeting
        if (signatureRepository.existsByMemberIdAndMinutesId(member, minutesTable)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Get minutes table object from meeting id
     * @param meetingId
     * @return minutes table object
     */
    private MinutesTable getMinutesTableFromMeetingId(Integer meetingId) {
        return minutesRepository.findMinutesTableByMeetingId(
                meetingRepository.findMeetingTableByMeetingId(meetingId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Meeting with id [%d] does not exist.".formatted(meetingId)
                        ))
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Minutes with meeting id [%d] does not exist.".formatted(meetingId)
        ));
    }

    /**
     * Get member object from member email
     *
     * @param memberEmail
     * @return member object
     */
    private Member getMemberFromMemberEmail(String memberEmail) {
        return memberRepository.findMemberByEmail(memberEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member with email [%s] does not exist.".formatted(memberEmail)
                ));
    }

    /**
     * Get meeting list that all attendee in a meeting already signed
     */
    public List<MeetingInfoDTO> checkAllAttendeeSigned() {
        // Get all meeting information
        List<MeetingTable> allMeeting = meetingRepository.findAll();

        // Get meeting list that all attendee in a meeting already signed
        List<MeetingTable> signedMeetingList = allMeeting.stream()
                .filter(this::isMinutesFullSigned)
                .toList();

        // Convert meeting table object to meeting info dto mapper object
        List<MeetingInfoDTO> signedMeetingListDTO = signedMeetingList.stream()
                .map(meetingInfoDTOMapper)
                .toList();

        return signedMeetingListDTO;
    }

    /**
     * check if all attendee in a meeting already signed minutes
     * @param meetingId
     * @return true if all attendee in a meeting already signed minutes
     */
    private boolean isMinutesFullSigned(MeetingTable meetingId) {
        // Get attendee list of the meeting
        List<AttendeeTable> attendeeList = attendeeRepository.findAllByMeetingId(meetingId);

        // Get signature list of the meeting
        List<SignatureInfo> signatureList = signatureRepository.findAllByMinutesId_MeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signature with meeting id [%d] does not exist.".formatted(meetingId.getMeetingId())
                ));

        // Check if all attendee in a meeting already signed
        for (AttendeeTable attendee : attendeeList) {
            for (SignatureInfo signature : signatureList) {
                if (attendee.getMemberId().getMemberId().equals(signature.getMemberId().getMemberId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get meeting list that there has at least one attendee in a meeting not signed
     */
    public List<MeetingInfoDTO> getNotAllAttendeeSigned() {
        // Get all meeting information
        List<MeetingTable> allMeeting = meetingRepository.findAll();

        // Get meeting list that there has at least one attendee in a meeting not signed
        List<MeetingTable> notSignedMeetingList = allMeeting.stream()
                .filter(this::isMinutesNotFullSigned)
                .toList();

        // Convert meeting table object to meeting info dto mapper object
        List<MeetingInfoDTO> notSignedMeetingListDTO = notSignedMeetingList.stream()
                .map(meetingInfoDTOMapper)
                .toList();

        return notSignedMeetingListDTO;
    }

    /**
     * check whether there has at least one attendee in a meeting not signed
     *
     * @param meetingTable
     * @return
     */
    private boolean isMinutesNotFullSigned(MeetingTable meetingTable) {
        // Get attendee list of the meeting
        List<AttendeeTable> attendeeList = attendeeRepository.findAllByMeetingId(meetingTable);

        // Get signature list of the meeting
        List<SignatureInfo> signatureList = signatureRepository.findAllByMinutesId_MeetingId(meetingTable)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signature with meeting id [%d] does not exist."
                                .formatted(meetingTable.getMeetingId())
                ));

        // Check if there has at least one attendee in a meeting not signed
        for (AttendeeTable attendee : attendeeList) {
            for (SignatureInfo signature : signatureList) {
                if (attendee.getMemberId().getMemberId().equals(signature.getMemberId().getMemberId())) {
                    return false;
                }}
        }
        return true;
    }

    // Get a table of whether meeting attendees have signed from meeting id
    public List<SignatureStatusDTO> getSignatureTableByMeetingId(Integer meetingId) {
        MeetingTable meetingTable = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meeting with id [%d] does not exist.".formatted(meetingId)
                ));

        // Get attendee whether signed from meeting id
        List<AttendeeTable> attendeeListByMeetingId =
                attendeeRepository.findAttendeeTableByMeetingId(meetingTable);

        List<SignatureStatusDTO> signatureStatusList = new ArrayList<>();
        for (AttendeeTable attendee : attendeeListByMeetingId) {
            Member member = attendee.getMemberId();
            String name = member.getFirstName() + " " + member.getLastName();
            String email = member.getEmail();
            String role = member.getMemberRole();
            Integer signatureStatus = isAttendeeSigned(meetingId, member.getMemberId()) ? 1 : 0;
            signatureStatusList.add(new SignatureStatusDTO(name, email, role, signatureStatus));
        }

        return signatureStatusList;
    }

    /**
     * Check if attendee has signed
     * @param meetingId
     * @param memberId
     * @return
     */
    private boolean isAttendeeSigned(Integer meetingId, Integer memberId) {
        MeetingTable selectedMeeting = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meeting with id [%d] does not exist.".formatted(meetingId)
                ));


        // Get signature record of selected meeting and memberId
        SignatureInfo signatureInfo =
                signatureRepository.findByMinutesId_MeetingIdAndMemberId_MemberId(selectedMeeting, memberId);

        // If signature record is null, return false
        return signatureInfo != null;
    }

    /**
     * According to memberEmail, getting meeting list that member attended and signatureInfo exists
     * @param memberEmail
     * @return MeetingInfoDTO list
     */
    public List<MeetingInfoDTO> getSignedMeetingListByMemberEmail(String memberEmail) {
        // Get member object from member email
        Member member = getMemberFromMemberEmail(memberEmail);

        // Get meeting list that member attended
        List<MeetingTable> meetingList = attendeeRepository.findAllByMemberId(member)
                .stream()
                .map(AttendeeTable::getMeetingId)
                .toList();

        // If signatureInfo exists, add meeting to list
        List<MeetingInfoDTO> signedMeetingList = new ArrayList<>();
        for (MeetingTable meeting : meetingList) {
            if (signatureRepository.existsByMemberIdAndMinutesId_MeetingId(member, meeting)) {
                signedMeetingList.add(meetingInfoDTOMapper.apply(meeting));
            }
        }

        return signedMeetingList;
    }

    /**
     * Get not signed meeting list by member email
     * @param memberEmail
     * @return
     */
    public List<MeetingInfoDTO> getNotSignedMeetingListByMemberEmail(String memberEmail) {
        // Get member object from member email
        Member member = getMemberFromMemberEmail(memberEmail);

        // Get meeting list that member attended
        List<MeetingTable> meetingList = attendeeRepository.findAllByMemberId(member)
                .stream()
                .map(AttendeeTable::getMeetingId)
                .toList();

        // If signatureInfo not exists, add meeting to list
        List<MeetingInfoDTO> notSignedMeetingList = new ArrayList<>();
        for (MeetingTable meeting : meetingList) {
            if (!signatureRepository.existsByMemberIdAndMinutesId_MeetingId(member, meeting)) {
                notSignedMeetingList.add(meetingInfoDTOMapper.apply(meeting));
            }
        }
        return notSignedMeetingList;
    }
}
