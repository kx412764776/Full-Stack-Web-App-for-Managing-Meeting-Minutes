package com.apprenticeship.integration;

import com.apprenticeship.dto.MeetingInfoDTO;
import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.requestsAndResponses.*;
import com.apprenticeship.service.MeetingService;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeetingIT {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MeetingService meetingService;

    private final String BASE_URL = "/apprenticeship/meeting";

    @BeforeEach
    void setUp() {
        // Register a test user
        // create a registration memberRegistrationRequest
//        MemberRegistrationRequest memberRegistrationRequest = new MemberRegistrationRequest(
//                "test", "test", "test@gmail.com", "test", "ACADEMIC"
//        );
//        webTestClient.post()
//                .uri("/apprenticeship/register")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                // set the body to memberRegistrationRequest
//                .body(Mono.just(memberRegistrationRequest), MemberRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

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
    void testGetAllMeetingInfo() {
        // given
        Date meetingDate = new Date();
        MeetingInfoDTO meetingInfoDTO = new MeetingInfoDTO(
                1,
                "meeting topic",
                "meeting name",
                meetingDate,
                "1 hour",
                "meeting description"
        );
        List<MeetingInfoDTO> meetings = List.of(meetingInfoDTO);
        when(meetingService.getAllMeetingInfo()).thenReturn(meetings);

        // when
        webTestClient.get()
                .uri(BASE_URL + "/meetingInfoList")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MeetingInfoDTO.class)
                .value(responseMeetings -> {
                    // Assert that the returned list of MeetingInfoDTO matches the mocked list
                    assertThat(responseMeetings).isEqualTo(meetings);
                });

    }

    @Test
    void testGetMeetingInfoByMeetingId() {

        // given
        Date meetingDate = new Date();
        MeetingInfoDTO meetingInfoDTO = new MeetingInfoDTO(
                1,
                "meeting topic",
                "meeting name",
                meetingDate,
                "1 hour",
                "meeting description"
        );
        when(meetingService.getMeetingInfoByMeetingId(1)).thenReturn(meetingInfoDTO);

        // when
        webTestClient.post()
                .uri(BASE_URL + "/meetingInfo/{meetingId}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MeetingInfoDTO.class)
                .value(responseMeeting -> {
                    // Assert that the returned MeetingInfoDTO matches the mocked MeetingInfoDTO
                    assertThat(responseMeeting).isEqualTo(meetingInfoDTO);
                });

    }

    @Test
    void testEditMeetingInfoByMeetingId() {

        // given
        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "meeting topic",
                "meeting name",
                meetingDate,
                "1 hour",
                "meeting description"
        );

        MeetingUpdateRequest meetingUpdateRequest = new MeetingUpdateRequest(
                "updated meeting topic",
                "updated meeting name",
                meetingDate,
                "2 hour",
                "updated meeting description"
        );

        when(meetingService.editMeetingInfoByMeetingId(1, meetingUpdateRequest)).thenReturn(new MeetingTable(
                1,
                "updated meeting topic",
                "updated meeting name",
                meetingDate,
                "2 hour",
                "updated meeting description"
        ));
        // when & then
        webTestClient.put()
                .uri(BASE_URL + "/meetingInfo/{meetingId}", 1)
                .body(Mono.just(meetingUpdateRequest), MeetingUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteMeetingInfoByMeetingId() {

        // given
        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "meeting topic",
                "meeting name",
                meetingDate,
                "1 hour",
                "meeting description"
        );

        // when
        webTestClient.delete()
                .uri(BASE_URL + "/meetingInfo/{meetingId}", 1)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetMeetingInfoByMemberEmail() {

        // given
        Date meetingDate = new Date();
        MeetingInfoDTO meetingInfoDTO = new MeetingInfoDTO(
                1,
                "meeting topic",
                "meeting name",
                meetingDate,
                "1 hour",
                "meeting description"
        );
        String memberEmail = "test@example.com";
        List<MeetingInfoDTO> meetings = List.of(meetingInfoDTO);
        when(meetingService.getMeetingInfoByMemberEmail(memberEmail)).thenReturn(meetings);

        // when
        webTestClient.post()
                .uri(BASE_URL + "/{memberEmail}", memberEmail)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MeetingInfoDTO.class)
                .value(responseMeetings -> {
                    // Assert that the returned list of MeetingInfoDTO matches the mocked list
                    assertThat(responseMeetings).isEqualTo(meetings);
                });
    }

    @Test
    void testGetMeetingInfoByMemberEmail_withNoMeetings_returnsNotFound() {

        // given
        String memberEmail = "test@example.com";
        when(meetingService.getMeetingInfoByMemberEmail(memberEmail)).thenReturn(null);

        // when
        webTestClient.post()
                .uri(BASE_URL + "/{memberEmail}", memberEmail)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testInsertMeetingInfo() {

        // given
        Date meetingDate = new Date();
        MeetingTable meeting = new MeetingTable(
                1,
                "meeting topic",
                "meeting name",
                meetingDate,
                "1 hour",
                "meeting description"
        );

        // when
        webTestClient.post()
                .uri(BASE_URL + "/meetingInfo")
                .body(Mono.just(meeting), MeetingTable.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testInsertAttendeeInfo() {

        // given
        AddAttendeeRequest addAttendeeRequest = new AddAttendeeRequest(
                List.of("test@example.com"),
                1
        );

        // when
        webTestClient.post()
                .uri(BASE_URL + "/attendee")
                .body(Mono.just(addAttendeeRequest), AddAttendeeRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testGetMemberInfoByMeetingId() {

        // given
        List<String> attendeeInfoList = List.of(
                "test-1@example.com, alex smith(apprentice)",
                "test-2@example.com, connor chen(mentor)"
        );
        when(meetingService.getAttendeeInfoByMeetingId(1)).thenReturn(attendeeInfoList);

        // when
        webTestClient.post()
                .uri(BASE_URL + "/attendee/{meetingId}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .value(responseAttendeeInfoList -> {
                    // Assert that the returned list of attendeeInfoList matches the mocked list
                    assertThat(responseAttendeeInfoList).isEqualTo(attendeeInfoList);
                });
    }

    @Test
    void testGetMemberInfoByMeetingId_withNoAttendees_returnsNotFound() {

        // given
        when(meetingService.getAttendeeInfoByMeetingId(1)).thenReturn(null);

        // when
        webTestClient.post()
                .uri(BASE_URL + "/attendee/{meetingId}", 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testRemoveAttendeeFromAttendeeTable() {

        // given
        List<String> memberEmails = List.of(
                "test-1@example.com",
                "test-2@example.com"
        );

        // when
        webTestClient.delete()
                .uri(BASE_URL + "/attendee/{meetingId}/{memberEmails}", 1, memberEmails)
                .exchange()
                .expectStatus().isOk();
    }

}
