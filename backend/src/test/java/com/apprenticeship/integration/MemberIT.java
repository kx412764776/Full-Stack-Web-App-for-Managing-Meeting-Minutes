package com.apprenticeship.integration;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberIT {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MemberService memberService;
    private final String BASE_URL = "/apprenticeship/member";

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test")
    void testGetMemberByEmail() {

        String memberEmail = "alex@example.com";
        MemberDTO memberDTO = new MemberDTO(
                1,
                "Alex",
                "Smith",
                memberEmail,
                List.of("APPRENTICE"),
                memberEmail
        );
        when(memberService.getMemberByEmail(memberEmail)).thenReturn(memberDTO);

        // Perform the request and validate the response
        webTestClient.post()
                .uri(BASE_URL + "/memberInfo/{memberEmail}", memberEmail)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(
                        """
                                {
                                  "memberId": 1,
                                  "firstName": "Alex",
                                  "lastName": "Smith",
                                  "email": "alex@example.com",
                                    "memberRoles": [
                                        "APPRENTICE"
                                    ],
                                    "username": "alex@example.com"
                                }
                                """
                );
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test")
    void testCheckMemberByEmail_withValidEmail_shouldReturnMemberInfo() {

        String memberEmailPrefix = "alex";
        List<String> memberEmailAndName = List.of("alex-1@gmail.com alex1", "alex-2@gmail.com alex2");
        when(memberService.checkMembersByEmailPrefix(memberEmailPrefix)).thenReturn(memberEmailAndName);

        // Perform the request and validate the response
        webTestClient.post()
                .uri(BASE_URL + "/checkMember/{memberEmailPrefix}", memberEmailPrefix)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(
                        """
                                [
                                    "alex-1@gmail.com alex1",
                                    "alex-2@gmail.com alex2"
                                ]
                                """
                );
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test")
    void testCheckMemberByEmail_withInvalidEmail_shouldReturnNotFound() {

        String memberEmailPrefix = "alex";
        when(memberService.checkMembersByEmailPrefix(memberEmailPrefix)).thenReturn(null);

        // Perform the request and validate the response
        webTestClient.post()
                .uri(BASE_URL + "/checkMember/{memberEmailPrefix}", memberEmailPrefix)
                .exchange()
                .expectStatus().isNotFound();
    }
}
