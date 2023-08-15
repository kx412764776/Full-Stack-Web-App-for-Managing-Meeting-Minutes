package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.MeetingInfoDTOMapper;
import com.apprenticeship.dto.SignatureStatusDTO;
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

    // store signature information according to meeting id and member email
    public void storeSignatureInfo(Integer meetingId, String memberEmail) {

        // get minutes object from meeting id
        MinutesTable minutesTable = getMinutesTableFromMeetingId(meetingId);

        // get member object from member email
        Member member = getMemberFromMemberEmail(memberEmail);

        // check if the member has already signed
        if (signatureRepository.existsByMemberIdAndMinutesId(member, minutesTable)) {
            throw new IllegalStateException(
                    "Member with email " + memberEmail + " has already signed."
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
                        .orElseThrow(() -> new IllegalStateException(
                                "Meeting with id " + meetingId + " does not exist."
                        ))
        ).orElseThrow(() -> new IllegalStateException(
                "Minutes with meeting id " + meetingId + " does not exist."
        ));
    }

    /**
     * Get member object from member email
     * @param memberEmail
     * @return member object
     */
    private Member getMemberFromMemberEmail(String memberEmail) {
        return memberRepository.findMemberByEmail(memberEmail)
                .orElseThrow(() -> new IllegalStateException(
                        "Member with email " + memberEmail + " does not exist."
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

    // check if all attendee in a meeting already signed
    private boolean isMinutesFullSigned(MeetingTable meetingId){
        // Get attendee list of the meeting
        List<AttendeeTable> attendeeList = attendeeRepository.findAllByMeetingId(meetingId);

        // Get signature list of the meeting
        List<SignatureInfo> signatureList = signatureRepository.findAllByMinutesId_MeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signature with meeting id " + meetingId + " does not exist."
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

    // Get a table of whether meeting attendees have signed from meeting id
    public List<SignatureStatusDTO> getSignatureTableByMeetingId(Integer meetingId) {
        MeetingTable meetingTable = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new IllegalStateException(
                        "Meeting with id " + meetingId + " does not exist."
                ));

        // Get attendee whether signed from meeting id
        List<AttendeeTable> attendeeListByMeetingId =
                attendeeRepository.findAttendeeTableByMeetingId(meetingTable);

        List<SignatureStatusDTO> signatureStatusList = new ArrayList<>();
        for (AttendeeTable attendee : attendeeListByMeetingId) {
            Member member = attendee.getMemberId();
            String name = member.getFirstName() + " " + member.getLastName();
            String email = member.getEmail();
            Integer signatureStatus = isAttendeeSigned(meetingId, member.getMemberId()) ? 1 : 0;
            signatureStatusList.add(new SignatureStatusDTO(name, email, signatureStatus));
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
                        "Meeting with id " + meetingId + " does not exist."
                ));


        // Get isSigned from signatureInfo table
        Integer isSigned = signatureRepository.findByMinutesId_MeetingIdAndMemberId_MemberId(selectedMeeting, memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Signature with meeting id " + meetingId + " and member id " + memberId + " does not exist."
        )).getIsSigned();

        return isSigned != null;
    }
}
