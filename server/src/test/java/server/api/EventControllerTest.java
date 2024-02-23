package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    /**
     * make the mock eventService available for use
     * initialise MockMvc with a specific controller instance
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    /**
     * simulate the delete request and check if receieves the status OK after the deletion
     * mimicking a call to a REST API
     * @throws Exception if the expected status is not OK or something went wrong in the test
     */
    @Test
    public void removeParticipant_ShouldReturnOk() throws Exception {
        long eventId = 1L;
        long participantId = 1L;

        doNothing().when(eventService).removeParticipantFromEvent(eventId, participantId);

        mockMvc.perform(delete("/api/events/{eventId}/participants/{participantId}", eventId, participantId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}