package com.apprenticeship.requestsAndResponses;

import java.util.Date;

public record MeetingUpdateRequest(
        String meetingTopic,
        String meetingName,
        Date meetingDate,
        String meetingDuration,
        String meetingDescription

) {
}
