package com.apprenticeship.controller;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.SignatureStatusDTO;
import com.apprenticeship.model.AttendeeTable;
import com.apprenticeship.repository.AttendeeRepository;
import com.apprenticeship.requestsAndResponses.AddMinutesContentRequest;
import com.apprenticeship.requestsAndResponses.SignatureRequest;
import com.apprenticeship.service.MinutesService;
import com.apprenticeship.service.SignatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is responsible for handling requests related to minutes table.
 */
@RestController
@RequestMapping("/apprenticeship/minutes")
public class MinutesController {
    private final MinutesService minutesService;
    private final SignatureService signatureService;

    public MinutesController(MinutesService minutesService,
                             SignatureService signatureService) {
        this.minutesService = minutesService;
        this.signatureService = signatureService;
    }

    // Insert minutes information according to meeting id and minutes content
    @PostMapping("/{meetingId}")
    public ResponseEntity<?> insertOrUpdateMinutesByMeetingId(
            @RequestBody AddMinutesContentRequest addMinutesContentRequest) {
        minutesService.insertOrUpdateMinuteByMeetingId(addMinutesContentRequest.meetingId(), addMinutesContentRequest.minutesContent());
        return ResponseEntity.ok().build();
    }

    // Get minutes information by meeting id
    @PostMapping("/meetingId/{meetingId}")
    public ResponseEntity<?> getMinutesByMeetingId(
            @PathVariable("meetingId") Integer meetingId) {
        String minutesContent = minutesService.getMinutesByMeetingId(meetingId);
        if (minutesContent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(minutesContent);
    }

    // store signature information according to meeting id and member email
    @PostMapping("/signature")
    public ResponseEntity<?> storeSignatureInfo(
            @RequestBody SignatureRequest signatureRequest) {

        signatureService.storeSignatureInfo(signatureRequest.meetingId(), signatureRequest.memberEmail());
        return ResponseEntity.ok().build();
    }

    // Get status of signature by meeting id and member email
    @PostMapping("/signature/status")
    public ResponseEntity<?> getSignatureStatus(
            @RequestBody SignatureRequest signatureRequest) {
        Integer signatureStatus = signatureService.getSignatureStatus(signatureRequest.meetingId(), signatureRequest.memberEmail());
        if (signatureStatus == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(signatureStatus);
    }

    // Get meeting list that all attendee in a meeting already signed
    @GetMapping("/signature/signed")
    public ResponseEntity<?> getSignedMeetingList() {
        // Get meeting list that all attendee in a meeting already signed
        List<MeetingInfoDTO> fullSignedMeetings = signatureService.checkAllAttendeeSigned();
        if (fullSignedMeetings == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(fullSignedMeetings);
    }

    // Get a table of whether meeting attendees have signed from meeting id
    @PostMapping("/signature/{meetingId}")
    public ResponseEntity<?> getSignatureTableByMeetingId(
            @PathVariable("meetingId") Integer meetingId) {
        List<SignatureStatusDTO> attendeeSignatureStatusList =
                signatureService.getSignatureTableByMeetingId(meetingId);

        return ResponseEntity.ok(attendeeSignatureStatusList);
    }

}
