package com.apprenticeship.integration;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.dto.SignatureStatusDTO;
import com.apprenticeship.requestsAndResponses.AddMinutesContentRequest;
import com.apprenticeship.requestsAndResponses.LoginRequest;
import com.apprenticeship.requestsAndResponses.LoginResponse;
import com.apprenticeship.requestsAndResponses.SignatureRequest;
import com.apprenticeship.service.MinutesService;
import com.apprenticeship.service.SignatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MinutesIT {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MinutesService minutesService;

    @MockBean
    private SignatureService signatureService;

    private static final String BASE_URL = "/apprenticeship/minutes";

    @BeforeEach
    void setUp() {
        // Login using the test user to get the JWT token
        LoginRequest loginRequest = new LoginRequest(
                "test@gmail.com",
                "test"
        );

        String jwtToken = webTestClient.post()
                .uri("/apprenticeship/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // set the body to loginRequest
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                // get the token from the login response header
                .expectBody(new ParameterizedTypeReference<LoginResponse>() {
                })
                .returnResult()
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // set the JWT token to the webTestClient
        webTestClient = webTestClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .build();
    }

    @Test
    void addMinutes_withValidData_shouldReturn200() {

        AddMinutesContentRequest request = new AddMinutesContentRequest(1, "Test minutes");

        webTestClient
                .post()
                .uri(BASE_URL + "/{meetingId}", 1)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(minutesService).insertOrUpdateMinuteByMeetingId(1, "Test minutes");

    }

    @Test
    void getMinutesByMeetingId_withValidId_shouldReturnMinutes() {

        when(minutesService.getMinutesByMeetingId(1))
                .thenReturn("Test minutes");

        webTestClient
                .post()
                .uri(BASE_URL + "/meetingId/{meetingId}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Test minutes");

    }

    @Test
    void getMinutesByMeetingId_withInvalidId_shouldReturn404() {

        webTestClient
                .post()
                .uri(BASE_URL + "/meetingId/{meetingId}", 1)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void storeSignature_withValidData_shouldReturn200() {

        SignatureRequest request = new SignatureRequest(1, "test@example.com");

        webTestClient
                .post()
                .uri(BASE_URL+ "/signature")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(signatureService).storeSignatureInfo(1, "test@example.com");

    }

    @Test
    void getSignatureStatus_withValidData_shouldReturn200() {

        SignatureRequest request = new SignatureRequest(1, "test@example.com");

        when(signatureService.getSignatureStatus(1, "test@example.com"))
                .thenReturn(1);

        webTestClient
                .post()
                .uri(BASE_URL+ "/signature/status")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(1);

    }

    @Test
    void getSignatureStatus_withInvalidData_shouldReturn404() {

        SignatureRequest request = new SignatureRequest(1, "test@example.com");

        when(signatureService.getSignatureStatus(1, "test@example.com"))
                .thenReturn(0);

        webTestClient
                .post()
                .uri(BASE_URL + "/signature/status")
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getFullSignedMeetings_shouldReturnMeetings() {

        List<MeetingInfoDTO> meetings = List.of(new MeetingInfoDTO(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        ));
        when(signatureService.checkAllAttendeeSigned()).thenReturn(meetings);

        webTestClient
                .get()
                .uri(BASE_URL + "/signature/signed")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MeetingInfoDTO.class)
                .isEqualTo(meetings);
    }

    @Test
    void getNotFullSignedMeetings_shouldReturnMeetings() {

        List<MeetingInfoDTO> meetings = List.of(new MeetingInfoDTO(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        ));
        when(signatureService.getNotAllAttendeeSigned()).thenReturn(meetings);

        webTestClient
                .get()
                .uri(BASE_URL + "/signature/notFullSigned")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MeetingInfoDTO.class)
                .isEqualTo(meetings);
    }

    @Test
    void getSignatureTable_withValidId_shouldReturnTable() {

        List<SignatureStatusDTO> table = List.of(
                new SignatureStatusDTO("Connor", "connor@example.com", "APPRENTICE", 1)
        );

        when(signatureService.getSignatureTableByMeetingId(1))
                .thenReturn(table);

        webTestClient
                .post()
                .uri(BASE_URL + "/signature/{meetingId}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SignatureStatusDTO.class)
                .contains(table.get(0));

    }

    @Test
    void getSignedMeetingsByMember_withValidEmail_shouldReturnMeetings() {

        List<MeetingInfoDTO> meetings = List.of(new MeetingInfoDTO(
                1,
                "test meeting topic",
                "test meeting name",
                new Date(),
                "1 hour",
                "test meeting description"
        ));
        when(signatureService.getSignedMeetingListByMemberEmail("test@example.com"))
                .thenReturn(meetings);

        webTestClient
                .post()
                .uri(BASE_URL + "/signature/signed/{memberEmail}", "test@example.com")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MeetingInfoDTO.class)
                .isEqualTo(meetings);

    }
}
