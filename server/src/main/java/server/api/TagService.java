package server.api;

import commons.Event;
import commons.Expense;
import commons.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;

import java.util.Set;

@Service
public class TagService {
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;
    private final TagRepository tagRepository;
    @Autowired
    public TagService(EventRepository eventRepository,
                      ExpenseRepository expenseRepository, TagRepository tagRepository){
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository;
        this.tagRepository = tagRepository;
    }

    public void addTagToEvent(String eventId, String tagName, String colorCode){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));;
        Tag tag = new Tag(tagName, colorCode);
        event.addTag(tag);
        eventRepository.save(event);
    }
    public void removeTag(String eventId, Long tagId){
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Set<Expense> expenses = event.getExpenses();
        Tag defaultTag = new Tag("Default", "#000000");
        for(Expense expense : expenses){
            if(expense.getExpenseTag().equals(tag)){
                expense.setExpenseTag(defaultTag);
                expenseRepository.save(expense);
            }
        }
        event.removeTag(tag);
        eventRepository.save(event);
    }

    public void addTagToExpense(String eventid, Long expenseId, String tagName, String colorCode){
        Event event = eventRepository.findById(eventid)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));
        if(!event.getExpenses().contains(expense)) {
            throw new EntityNotFoundException("Expense not found in the specified event");
        }
        Tag tag = new Tag(tagName, colorCode);
        expense.setExpenseTag(tag);
        eventRepository.save(event);
    }

    public Tag editTagOfExpense(String eventid, Long expenseId, String tagName, String colorCode){
        Event event = eventRepository.findById(eventid)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));
        if(!event.getExpenses().contains(expense)) {
            throw new EntityNotFoundException("Expense not found in the specified event");
        }
        Tag tag = expense.getExpenseTag();
        tag.setTagName(tagName);
        tag.setColorCode(colorCode);
        return tagRepository.save(tag);
    }
}
