package server.api;

import commons.Event;
import commons.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    /**
     * Tests adding a tag to an event that exists.
     */
    @Test
    void addTagToExistingEvent() {
        Event event = new Event("Event1", null);
        String eventId = "1";
        String tagName = "Important";
        String colorCode = "#FF0000";

        when(eventRepository.findById(anyString())).thenReturn(Optional.of(event));
        doAnswer(invocation -> {
            Event savedEvent = invocation.getArgument(0);
            savedEvent.addTag(new Tag(tagName, colorCode));
            return null;
        }).when(eventRepository).save(any(Event.class));

        tagService.addTagToEvent(eventId, tagName, colorCode);

        verify(eventRepository).save(eventCaptor.capture());
        assertEquals(1, eventCaptor.getValue().getEventTags().size());
        assertTrue(eventCaptor.getValue().getEventTags().iterator().next().getTagName().equals(tagName));
    }
}
