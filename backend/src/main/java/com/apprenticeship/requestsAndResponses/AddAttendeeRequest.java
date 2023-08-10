package com.apprenticeship.requestsAndResponses;

import java.util.List;

public record AddAttendeeRequest(
        List<String> emails,
        Integer meetingId
) {
}
