package it.lessons.spring.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.lessons.spring.ticket_platform.model.Ticket;
import it.lessons.spring.ticket_platform.model.User;
import it.lessons.spring.ticket_platform.repository.TicketRepository;
import it.lessons.spring.ticket_platform.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public UserService(UserRepository userRepository, TicketRepository ticketRepository){
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    //Cerca l'utente tramite username
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    //Recupera tutti i ticket assegnati all’utente con quel username
    //Controlla se ce ne sono con stato "NEW" o "IN_PROGRESS"
    //Se ce n’è anche solo uno, NON può modificare lo stato
    public boolean canModifyStatus(String username){
        List<Ticket> tickets = ticketRepository.findByUser_Username(username);
        for(Ticket t : tickets){
            if(t.getTicketStatus().equals("NEW") || t.getTicketStatus().equals("IN_PROGRESS")){
                return false;
            }
        }
        return true;
    }

    //Inverte lo stato true/false e viceversa
    public boolean toggleOperatorStatus(User user){
        user.setOperatorStatus(!user.getOperatorStatus());
        userRepository.save(user);
        return user.getOperatorStatus(); // restituisce il nuovo stato (true = attivo)
    }

    public List<User> findAvailableOperators(){
    return userRepository.findByOperatorStatusTrueAndRoles_Name("OPERATOR");
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}