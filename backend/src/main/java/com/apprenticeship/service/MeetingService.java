package com.apprenticeship.service;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.MeetingInfoDTOMapper;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.AttendeeTable;
import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.Member;
import com.apprenticeship.repository.AttendeeRepository;
import com.apprenticeship.repository.MeetingRepository;
import com.apprenticeship.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class is for implementing the business logic of the MeetingController
 */
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final AttendeeRepository attendeeRepository;

    private final MeetingInfoDTOMapper meetingInfoDTOMapper;

    public MeetingService(MeetingRepository meetingRepository,
                          MemberRepository memberRepository,
                          AttendeeRepository attendeeRepository,
                          MeetingInfoDTOMapper meetingInfoDTOMapper) {
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.attendeeRepository = attendeeRepository;
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

    public MeetingTable insertMeetingInfo(MeetingTable meetingTableInfo) {
        meetingRepository.save(meetingTableInfo);
        return meetingTableInfo;
    }

    // Get all meetings information
    public List<MeetingInfoDTO> getAllMeetingInfo() {
        List<MeetingInfoDTO> meetingInfoList = meetingRepository.findAll()
                .stream()
                .map(meetingInfoDTOMapper)
                .toList();
        return meetingInfoList;
    }

    // According to the email and meetingId to insert the information to the attendee table
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
                throw new IllegalStateException(
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

    // According to the meetingId to get the attendee information
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

    // According to the meetingId and memberEmails to delete the information from the attendee table
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
                throw new IllegalStateException(
                        ("member with email [%s] not exists in meeting with meetingId [%s]"
                                .formatted(member.getEmail(), meetingId))
                );
            }
            attendeeRepository.deleteDistinctByMemberIdAndMeetingId(member, meetingTableInfo);
        });

    }

    public MeetingInfoDTO getMeetingInfoByMeetingId(Integer meetingId) {
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));
        MeetingInfoDTO meetingInfoDTO = meetingInfoDTOMapper.apply(meetingTableInfo);
        return meetingInfoDTO;
    }

    public MeetingTable getMeetingInfoById(Integer meetingId) {
        MeetingTable meetingTableInfo = meetingRepository.findMeetingTableByMeetingId(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("meeting with meetingId [%s] not found".formatted(meetingId))
                ));
        return meetingTableInfo;
    }
}
