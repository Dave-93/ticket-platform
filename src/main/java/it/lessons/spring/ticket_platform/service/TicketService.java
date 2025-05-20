package it.lessons.spring.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.lessons.spring.ticket_platform.model.Ticket;
import it.lessons.spring.ticket_platform.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final NoteService noteService;

    public TicketService(TicketRepository ticketRepository, NoteService noteService){
        this.ticketRepository = ticketRepository;
        this.noteService = noteService;
    }

    //Restituisce tutti i ticket nel db
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    //Restituisce solo i ticket associati a un utente specifico, cercato per username
    public List<Ticket> getTicketsByUser(String username) {
        return ticketRepository.findByUser_Username(username);
    }

    //Cerca i ticket il cui titolo contiene una keyword, se non presente mostra tutta la lista
    public List<Ticket> searchTicketsByTitle(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return ticketRepository.findByTitleContainingIgnoreCase(keyword);
        }
        return ticketRepository.findAll();
    }

    //Cerca un ticket per id
    public Optional<Ticket> getTicketById(Integer id) {
        return ticketRepository.findById(id);
    }

    //Crea il ticket o aggiorna se esistente
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    //Elimina ticket e le note a lui associate
    public void deleteTicket(Integer id) {
        Optional<Ticket> ticketRemove = ticketRepository.findById(id);
        if(ticketRemove.isPresent()){
            Ticket ticket = ticketRemove.get();
            noteService.deleteNotesByTicket(ticket);//Cancella le note
            ticketRepository.delete(ticket);//Cancella il ticket
        }
    }
}