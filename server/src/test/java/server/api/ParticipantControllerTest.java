package server.api;
import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParticipantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    /**
     * make the mock eventService available for use
     * initialise MockMvc with a specific controller instance
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }

    /**
     * simulate the delete request and check if receieves the status OK after the deletion
     * mimicking a call to a REST API
     * @throws Exception if the expected status is not OK or something went wrong in the test
     */
    @Test
    public void removeParticipant_ShouldReturnOk() throws Exception {
        long participantId = 1L;

        doNothing().when(participantService).removeParticipant(participantId);

        mockMvc.perform(delete("/api/participants/{participantId}", participantId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * check to see if we get an ok response after changing the name of a participant
     * and if the name is correctly changed
     */
    @Test
    public void editParticipantOkResponseFromServer() {
        String eventCode = "RandomCode123";
        Participant participantDetails = new Participant("Jane Doe", new Event("Sample Event", eventCode));
        given(participantService.editParticipant(anyLong(), any(Participant.class)))
                .willReturn(participantDetails);
        ResponseEntity<Participant> response = participantController.editParticipant(participantDetails.getId(), participantDetails);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Jane Doe", response.getBody().getName());
    }
}