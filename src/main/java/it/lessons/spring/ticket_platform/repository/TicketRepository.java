package it.lessons.spring.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.spring.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer>{

    //Restituisce tutti i ticket il cui titolo contiene la parola chiave passata
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);

    //Restituisce tutti i ticket associati ad un utente con uno specifico username
    List<Ticket> findByUser_Username(String username);

}