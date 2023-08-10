package com.apprenticeship.requestsAndResponses;

public record AddMinutesContentRequest(
        Integer meetingId,
        String minutesContent
) {
}
