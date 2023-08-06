package com.apprenticeship.requestsAndResponses;

import java.util.List;

public record addAttendeeRequest(
        List<String> emails,
        Integer meetingId
) {
}
