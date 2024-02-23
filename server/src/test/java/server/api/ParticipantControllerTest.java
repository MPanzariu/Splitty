package server.api;
import commons.Event;
import commons.Participant;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
        Participant participantDetails = new Participant("Jane Doe", new Event("Sample Event"));
        given(participantService.editParticipant(anyLong(), any(Participant.class)))
                .willReturn(participantDetails);
        ResponseEntity<Participant> response = participantController.editParticipant(participantDetails.getId(), participantDetails);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Jane Doe", response.getBody().getName());
    }

    /**
     * check if the error is successfully thrown for a non-existent participant
     */
    @Test
    public void editParticipantNonExistent() {
        Long nonExistentId = 999L;
        Participant participantDetails = new Participant("Jane Doe", new Event("Sample Event"));

        given(participantService.editParticipant(eq(nonExistentId), any(Participant.class)))
                .willThrow(new EntityNotFoundException("Participant not found"));

        assertThrows(EntityNotFoundException.class, () -> {
            participantController.editParticipant(nonExistentId, participantDetails);
        }, "Should throw EntityNotFoundException for a nonexistent participant.");
    }
    /**
     * test if the service method is called with the correct parameters.
     */
    @Test
    public void editParticipantVerifyDetails() {
        Long participantId = 1L;
        Participant participantDetails = new Participant("Jane Doe", new Event("Sample Event"));
        given(participantService.editParticipant(eq(participantId), any(Participant.class)))
                .willAnswer(invocation -> invocation.getArgument(1));
        ResponseEntity<Participant> response = participantController.editParticipant(participantId, participantDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participantDetails.getName(), response.getBody().getName());
        verify(participantService).editParticipant(eq(participantId), argThat(
                p -> "Jane Doe".equals(p.getName()) && "Sample Event".equals(p.getEvent().getTitle())
        ));
    }
}