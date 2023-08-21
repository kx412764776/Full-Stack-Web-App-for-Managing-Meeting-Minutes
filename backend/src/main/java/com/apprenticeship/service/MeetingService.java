package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.MeetingInfoDTOMapper;
import com.apprenticeship.exception.RequestException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.*;
import com.apprenticeship.repository.*;
import com.apprenticeship.requestsAndResponses.MeetingUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * This class is for implementing the business logic of the MeetingController
 */
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final AttendeeRepository attendeeRepository;
    private final MinutesRepository minutesRepository;
    private final SignatureRepository signatureRepository;
    private final MeetingInfoDTOMapper meetingInfoDTOMapper;

    public MeetingService(MeetingRepository meetingRepository,
                          MemberRepository memberRepository,
                          AttendeeRepository attendeeRepository,
                          MinutesRepository minutesRepository,
                          SignatureRepository signatureRepository,
                          MeetingInfoDTOMapper meetingInfoDTOMapper) {
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.attendeeRepository = attendeeRepository;
        this.minutesRepository = minutesRepository;
        this.signatureRepository = signatureRepository;
        this.meetingInfoDTOMapper = meetingInfoDTOMapper;
    }

    /**
     * This method is used to get the meeting information by login member email
     * @param memberEmail
     * @return MeetingInfoDTO List from the MeetingTable
     */
    public List<MeetingInfoDTO> getMeetingInfoByMemberEmail(String memberEmail) {
        // According to the email in the member table to find the corresponding memberID
        Member member = memberRepository.findMemberByEmail(memberEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("member with email [%s] not found".formatted(memberEmail))
                ));
        // According to the memberID from the attendee table to find the corresponding meetingInfo list
        List<MeetingInfoDTO> meetingInfoList = attendeeRepository.findAllByMemberId(member)
                .stream()
                .map(AttendeeTable::getMeetingId)
                .map(meetingInfoDTOMapper)
                .toList();

        return meetingInfoList;
    }

    /**
     * This method is used to insert the meeting information to the meeting table
     */
    public MeetingTable insertMeetingInfo(MeetingTable meetingTableInfo) {
        meetingRepository.save(meetingTableInfo);
        return meetingTableInfo;
    }

    /**
     * Get all meeting information from the meeting table
     */
    public List<MeetingInfoDTO> getAllMeetingInfo() {
        List<MeetingInfoDTO> meetingInfoList = meetingRepository.findAll()
                .stream()
                .map(meetingInfoDTOMapper)
                .toList();
        return meetingInfoList;
    }

    /**
     * According to the email and meetingId to insert the information to the attendee table
     * @param email email list from request body
     * @param meetingId meetingId from request body
     * @return attendee info list
     */
    public List<AttendeeTable> insertAttendeeInfo(List<String> email, Integer meetingId) {

        // Step1: According to the every email in the member table
        // by separate email list to find every corresponding memberID
        List<Member> members = email.stream()
                .map(memberRepository::findMemberByEmail)
                .map(member -> member.orElseThrow(() -> new ResourceNotFoundException(
                        ("member with email [%s] not found".formatted(member))
                )))
                .toList();

        // Step 2: According to the meetingId to find the corresponding meeting information
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));

        //Step 3: If the attendee table already has the same meetingId and memberId, then throw exception
        members.forEach(member -> {
            if (attendeeRepository.existsByMemberIdAndMeetingId(member, meetingTableInfo)) {
                throw new RequestException(
                        ("member with email [%s] already exists in meeting with meetingId [%s]"
                                .formatted(member.getEmail(), meetingId))
                );
            }
        });

        // Step4: Insert the information to the attendee table
        List<AttendeeTable> attendeeTableList = members.stream()
                .map(member -> attendeeRepository.save(
                        new AttendeeTable(meetingTableInfo, member)
                ))
                .toList();
        return attendeeTableList;
    }

    /**
     * According to the meetingId to get the attendee information
      * @param meetingId meetingId from request body
     * @return member email, firstName, lastName and memberRole String list
     */
    public List<String> getAttendeeInfoByMeetingId(Integer meetingId) {
        // According to the meetingId to find the corresponding meeting information
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));

        // According to the meetingId to find the corresponding attendee information
        List<Member> attendeeInfoList = attendeeRepository.findAllByMeetingId(meetingTableInfo)
                .stream()
                .map(AttendeeTable::getMemberId)
                .toList();

        // According to the attendee information to get the email, firstName and lastName
        List<String> attendeeInfo = attendeeInfoList.stream()
                .map(member ->
                        member.getEmail() + "," +
                        member.getFirstName() + " " +
                        member.getLastName() + "(" +
                        member.getMemberRole() + ")" )
                .toList();

        return attendeeInfo;
    }

    /**
     * According to the meetingId and memberEmails to delete the information from the attendee table
     * @param meetingId meetingId from request body
     * @param memberEmails memberEmails from request body
     */
    @Transactional(
            rollbackFor = {IllegalStateException.class, ResourceNotFoundException.class}
    )
    public void removeAttendeeFromAttendeeTable(Integer meetingId, List<String> memberEmails) {
        // According to memberEmails to find the corresponding memberID
        List<Member> members = memberEmails.stream()
                .map(memberRepository::findMemberByEmail)
                .map(member -> member.orElseThrow(() -> new ResourceNotFoundException(
                        ("member with email [%s] not found".formatted(member))
                )))
                .toList();

        // According to the meetingId to find the corresponding meeting information
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));

        // According to the meetingId and memberId to delete the information from the attendee table
        members.forEach(member -> {
            if (!attendeeRepository.existsByMemberIdAndMeetingId(member, meetingTableInfo)) {
                throw new ResourceNotFoundException(
                        ("member with email [%s] does not exist in meeting with meetingId [%s]"
                                .formatted(member.getEmail(), meetingId))
                );
            }
            attendeeRepository.deleteDistinctByMemberIdAndMeetingId(member, meetingTableInfo);
        });

    }

    /**
     * According to the meetingId to get the meeting information
     */
    public MeetingInfoDTO getMeetingInfoByMeetingId(Integer meetingId) {
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));
        MeetingInfoDTO meetingInfoDTO = meetingInfoDTOMapper.apply(meetingTableInfo);
        return meetingInfoDTO;
    }

    /**
     * According to the meetingId to get the meeting information
     */
    public MeetingTable getMeetingInfoById(Integer meetingId) {
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));
        return meetingTableInfo;
    }

    /**
     * This method is used to edit the meeting information by meetingId
     * @param meetingId
     * @param updatedMeetingTableInfo the updated meeting information from frontend
     * @return the meeting information after edit
     */
    public MeetingTable editMeetingInfoByMeetingId(Integer meetingId, MeetingUpdateRequest updatedMeetingTableInfo) {
        // According to the meetingId to find the corresponding meeting information
        MeetingTable meetingInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));

        // Update the meeting information
        boolean changes = false;

        // determine if the meeting topic is null or if it is not changed
        if (updatedMeetingTableInfo.meetingTopic() != null && !updatedMeetingTableInfo.meetingTopic().equals(meetingInfo.getMeetingTopic())) {
            meetingInfo.setMeetingTopic(updatedMeetingTableInfo.meetingTopic());
            changes = true;
        }

        // determine if the meeting name is null or if it is not changed
        if (updatedMeetingTableInfo.meetingName() != null && !updatedMeetingTableInfo.meetingName().equals(meetingInfo.getMeetingName())) {
            meetingInfo.setMeetingName(updatedMeetingTableInfo.meetingName());
            changes = true;
        }

        // determine if the meeting date is null or if it is not changed
        if (updatedMeetingTableInfo.meetingDate() != null && !updatedMeetingTableInfo.meetingDate().equals(meetingInfo.getMeetingDate())) {
            meetingInfo.setMeetingDate(updatedMeetingTableInfo.meetingDate());
            changes = true;
        }

        // determine if the meeting duration is null or if it is not changed
        if (updatedMeetingTableInfo.meetingDuration() != null && !updatedMeetingTableInfo.meetingDuration().equals(meetingInfo.getMeetingDuration())) {
            meetingInfo.setMeetingDuration(updatedMeetingTableInfo.meetingDuration());
            changes = true;
        }

        // If there is no change, throw exception
        if (!changes) {
            throw new RequestException("Meeting information is not changed with meetingId [%s]".formatted(meetingId));
        }

        meetingRepository.save(meetingInfo);
        return meetingInfo;

    }

    /**
     * This method is used to delete the meeting information by meetingId
     */
    public void deleteMeetingInfoByMeetingId(Integer meetingId) {
        // According to the meetingId to find the corresponding meeting information
        MeetingTable meetingInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));

        // According to the meetingId to find the corresponding attendee information and delete
        List<AttendeeTable> attendeeInfoList = attendeeRepository.findAttendeeTableByMeetingId(meetingInfo);
        if (attendeeInfoList != null) {
            attendeeRepository.deleteAll(attendeeInfoList);
        }

        // According to the meetingId to find the corresponding minutes information and delete
        Optional<MinutesTable> minutesInfo = minutesRepository.findMinutesTableByMeetingId(meetingInfo);
        minutesInfo.ifPresent(minutesRepository::delete);

        // According to the meetingId to find the corresponding signature information and delete
        Optional<List<SignatureInfo>> signatureInfoList =
                signatureRepository.findAllByMinutesId_MeetingId(meetingInfo);
        signatureInfoList.ifPresent(signatureRepository::deleteAll);

        meetingRepository.delete(meetingInfo);
    }
}
