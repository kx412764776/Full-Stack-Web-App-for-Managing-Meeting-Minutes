package com.apprenticeship.controller;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.exception.RequestException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.requestsAndResponses.AddAttendeeRequest;
import com.apprenticeship.requestsAndResponses.MeetingUpdateRequest;
import com.apprenticeship.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is responsible for handling all requests related to meetings.
 */
@RestController
@RequestMapping("/apprenticeship/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // Get all meetings information
    @GetMapping("/meetingInfoList")
    public ResponseEntity<?> getAllMeetingInfo() {
        List<MeetingInfoDTO> meetingInfoDTO = meetingService.getAllMeetingInfo();
        if (meetingInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetingInfoDTO);
    }

    // Get meeting information by meeting id
    @PostMapping("/meetingInfo/{meetingId}")
    public ResponseEntity<?> getMeetingInfoByMeetingId(
            @PathVariable("meetingId") Integer meetingId) {
        MeetingInfoDTO meetingInfoDTO = meetingService.getMeetingInfoByMeetingId(meetingId);
        if (meetingInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetingInfoDTO);
    }

    // Edit meeting information by meeting id
    @PutMapping("/meetingInfo/{meetingId}")
    public void editMeetingInfoByMeetingId(
            @PathVariable("meetingId") Integer meetingId,
            @RequestBody MeetingUpdateRequest meetingUpdateRequest) {
        meetingService.editMeetingInfoByMeetingId(meetingId, meetingUpdateRequest);
    }

    // Delete meeting information by meeting id
    @DeleteMapping("/meetingInfo/{meetingId}")
    public void deleteMeetingInfoByMeetingId(
            @PathVariable("meetingId") Integer meetingId) {
        meetingService.deleteMeetingInfoByMeetingId(meetingId);
    }

    // Get meetings information by member email
    @PostMapping({"/{memberEmail}"})
    public ResponseEntity<?> getMeetingInfoByMemberEmail(
            @PathVariable("memberEmail") String memberEmail) {
        List<MeetingInfoDTO> meetingInfoDTO = meetingService.getMeetingInfoByMemberEmail(memberEmail);
        if (meetingInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetingInfoDTO);

    }

    // Insert meeting information to database
    @PostMapping
    public ResponseEntity<?> insertMeetingInfo(@RequestBody MeetingTable meetingTableInfo) {
        MeetingTable insertMeetingInfo = meetingService.insertMeetingInfo(meetingTableInfo);
        return new ResponseEntity<>(insertMeetingInfo, HttpStatus.CREATED);
    }

    // According to the member email and meeting id to insert attendee information to database
    @PostMapping("/attendee")
    public ResponseEntity<?> insertAttendeeInfo(@RequestBody AddAttendeeRequest addAttendeeRequest) {
        try {
            meetingService.insertAttendeeInfo(
                    addAttendeeRequest.emails().stream().toList(),
                    addAttendeeRequest.meetingId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        }
    }

    // Get member info from memberId in attendee Table according to meetingId
    @PostMapping("/attendee/{meetingId}")
    public ResponseEntity<?> getMemberInfoByMeetingId(
            @PathVariable("meetingId") Integer meetingId) {
        List<String> attendeeInfoByMeetingId = meetingService.getAttendeeInfoByMeetingId(meetingId);
        if (attendeeInfoByMeetingId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attendeeInfoByMeetingId);
    }

    // Remove attendee from attendee table
    @DeleteMapping("/attendee/{meetingId}/{memberEmails}")
    public ResponseEntity<?> removeAttendeeFromAttendeeTable(
            @PathVariable("meetingId") Integer meetingId,
            @PathVariable("memberEmails") List<String> memberEmails) {
        try {
            meetingService.removeAttendeeFromAttendeeTable(meetingId, memberEmails);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
