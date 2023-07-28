package com.apprenticeship.controller;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class is responsible for handling all requests related to meetings.
 */
@RestController
@RequestMapping("/apprenticeship/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController( MeetingService meetingService) {
        this.meetingService = meetingService;
    }


    // Get meetings information by member email
    @PostMapping({"/{memberEmail}"})
    public ResponseEntity<?> getMeetingInfoByMemberEmail(@PathVariable String memberEmail) {
        List<MeetingInfoDTO> meetingInfoDTO = meetingService.getMeetingInfoByMemberEmail(memberEmail);
        if (meetingInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetingInfoDTO);

    }

}
