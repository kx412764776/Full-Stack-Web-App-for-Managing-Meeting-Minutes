package com.apprenticeship.requestsAndResponses;

import java.util.List;

public record NotificationRequest(
        Integer meetingId,
        List<String> memberEmails
) {
}
