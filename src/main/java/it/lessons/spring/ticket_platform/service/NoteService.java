package it.lessons.spring.ticket_platform.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.lessons.spring.ticket_platform.model.Note;
import it.lessons.spring.ticket_platform.model.Ticket;
import it.lessons.spring.ticket_platform.model.User;
import it.lessons.spring.ticket_platform.repository.NoteRepository;
import it.lessons.spring.ticket_platform.repository.TicketRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final TicketRepository ticketRepository;

    public NoteService(NoteRepository noteRepository, TicketRepository ticketRepository) {
        this.noteRepository = noteRepository;
        this.ticketRepository = ticketRepository;
    }

    //Preparo nuova nota con ticket e data odierna
    public Note createNoteForTicket(Integer ticketId) {
        Note note = new Note();
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        ticket.ifPresent(note::setTicket);
        note.setNoteDate(LocalDate.now());
        return note;
    }

    //Imposto l'username e data corrente durante il salvataggio
    public Note saveNote(Note note, User user) {
        note.setUser(user);
        note.setNoteDate(LocalDate.now()); // aggiorna comunque alla data attuale
        return noteRepository.save(note);
    }

    //Cerco una nota nel db tramite id
    public Optional<Note> findById(Integer id) {
        return noteRepository.findById(id);
    }

    public void deleteNotesByTicket(Ticket ticket) {
        // Trova le note associate al ticket
        List<Note> notes = noteRepository.findByTicket(ticket);
        // Elimina ogni nota singolarmente
        for (Note note : notes) {
            noteRepository.delete(note);
        }
    }
}