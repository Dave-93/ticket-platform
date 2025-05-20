package it.lessons.spring.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.spring.ticket_platform.model.Note;
import it.lessons.spring.ticket_platform.model.Ticket;

public interface NoteRepository extends JpaRepository<Note, Integer>{

    //Restituisce tutte le note associate al ticket
    List<Note> findByTicket(Ticket ticket);
}