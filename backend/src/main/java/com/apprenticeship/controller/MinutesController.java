package com.apprenticeship.controller;

import com.apprenticeship.requestsAndResponses.AddMinutesContentRequest;
import com.apprenticeship.service.MinutesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is responsible for handling requests related to minutes table.
 */
@RestController
@RequestMapping("/apprenticeship/minutes")
public class MinutesController {
    private final MinutesService minutesService;

    public MinutesController(MinutesService minutesService) {
        this.minutesService = minutesService;
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
}
