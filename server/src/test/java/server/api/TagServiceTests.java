package server.api;

import commons.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TagServiceTests {
    private TagRepository tagRepository;
    private EventRepository eventRepository;
    private ExpenseRepository expenseRepository;
    private TagService service;

    /**
     * Test setup
     */
    @BeforeEach
    public void setup() {
        tagRepository = mock(TagRepository.class);
        eventRepository = mock(EventRepository.class);
        expenseRepository = mock(ExpenseRepository.class);
        service = new TagService(eventRepository, expenseRepository, tagRepository);
    }

    /**
     * The tag received from the repository should have the same name and color as the new tag.
     * Tag repository should save updated tag.
     */
    @Test
    public void editExistingTag() {
        long id = 5;
        Tag newTag = new Tag("Food", "#FFFFFF");
        Tag oldTag = new Tag("Travel", "#000000");
        when(tagRepository.findById(id)).thenReturn(Optional.of(oldTag));
        service.editTag(id, newTag);
        assertEquals(oldTag, newTag);
        verify(tagRepository).save(oldTag);
    }

    /**
     * EntityNotFoundException should be thrown when a tag is not found by the given ID.
     * The exception message should indicate that it cannot be found.
     */
    @Test
    public void editNonExistingTag() {
        long id = 5;
        Tag newTag = new Tag("Food", "#FFFFFF");
        when(tagRepository.findById(id)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                service.editTag(id, newTag));
        assertEquals(exception.getMessage(), "Tag is not found");
    }
}
