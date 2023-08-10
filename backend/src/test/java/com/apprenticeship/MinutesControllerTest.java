package com.apprenticeship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MinutesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testInsertMinutesByMeetingId() throws Exception {
        String minutesContent = "Version 4: This is a test minutes content.";

        ResultActions result = mockMvc.perform(post("/apprenticeship/minutes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"meetingId\": 1, \"minutesContent\": \"" + minutesContent + "\"}")
        );

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetMinutesByMeetingId() throws Exception {
        ResultActions result = mockMvc.perform(post("/apprenticeship/minutes/meetingId/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }
}
