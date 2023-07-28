package com.apprenticeship.dto;

import java.util.Date;

public record MeetingInfoDTO(
        Integer meetingId,
        String meetingTopic,
        String meetingName,
        Date meetingDate,
        String meetingDuration,
        String meetingDescription
) {
}
