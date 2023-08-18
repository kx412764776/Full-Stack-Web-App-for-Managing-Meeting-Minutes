package com.apprenticeship.integration;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.requestsAndResponses.LoginRequest;
import com.apprenticeship.requestsAndResponses.LoginResponse;
import com.apprenticeship.requestsAndResponses.MemberRegistrationRequest;
import com.apprenticeship.utils.JWTUtil;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private static final String PROJECT_PATH = "/apprenticeship";

    @Test
    void testLogin() {
        // Given
        // create a registration memberRegistrationRequest
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "-" + UUID.randomUUID() + "@example.com";
        String password = "password";
        String memberRole = "APPRENTICE";

        MemberRegistrationRequest memberRegistrationRequest = new MemberRegistrationRequest(
                firstName, lastName, email, password, memberRole
        );

        LoginRequest loginRequest = new LoginRequest(
                email, password
        );

        // send login request without authentication
        // When
        webTestClient.post()
                .uri(PROJECT_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // set the body to loginRequest
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                // Then
                .expectStatus()
                .isUnauthorized();

        // send a post memberRegistrationRequest to register a member
        // When
        webTestClient.post()
                .uri(PROJECT_PATH + "/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // set the body to memberRegistrationRequest
                .body(
                        Mono.just(memberRegistrationRequest),
                        MemberRegistrationRequest.class
                )
                .exchange()
                // Then
                .expectStatus()
                .isOk();

        // get the token from login response
        EntityExchangeResult<LoginResponse> result = webTestClient.post()
                .uri(PROJECT_PATH + "/login")
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
                .returnResult();


        // get the login response body(memberDTO and token)
        LoginResponse loginResponse = result.getResponseBody();

        // verify the login response body is exist
        assertThat(loginResponse).isNotNull();

        // get the memberDTO from the login response body
        MemberDTO memberDTO = loginResponse.memberDTO();

        assertThat(memberDTO).isNotNull();

        // verify the memberDTO
        assertThat(memberDTO.firstName()).isEqualTo(firstName);
        assertThat(memberDTO.lastName()).isEqualTo(lastName);
        assertThat(memberDTO.email()).isEqualTo(email);
        assertThat(memberDTO.memberRoles()).contains(memberRole);
        assertThat(memberDTO.username()).isEqualTo(email);


        // get the token from the login response header
        String jwtToken = result.getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // verify the token
        assertThat(jwtToken).isNotNull();
        assertThat(jwtUtil.isTokenValid(
                jwtToken,
                memberRegistrationRequest.email())).isTrue();

    }
}
