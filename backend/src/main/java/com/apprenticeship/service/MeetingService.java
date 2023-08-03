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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is for implementing the business logic of the MeetingController
 */
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final AttendeeRepository attendeeRepository;

    private final MeetingInfoDTOMapper meetingInfoDTOMapper;

    @Autowired
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
}
