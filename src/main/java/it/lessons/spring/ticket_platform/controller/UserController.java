package it.lessons.spring.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.spring.ticket_platform.model.Ticket;
import it.lessons.spring.ticket_platform.model.User;
import it.lessons.spring.ticket_platform.repository.TicketRepository;
import it.lessons.spring.ticket_platform.repository.UserRepository;


@Controller
public class UserController {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    
    @Autowired//se una classe ha un solo costruttore, Spring lo usa automaticamente per l'iniezione dei bean, anche senza l’annotazione @Autowired
    public UserController(UserRepository userRepository, TicketRepository ticketRepository){
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/modificaStato")
    public String modificaStato(Authentication authentication, Model model, RedirectAttributes redirectAttributes){
        String username = authentication.getName();//Recupero lo username dell'utente loggato
        User user = userRepository.findByUsername(username).orElse(null);//Recupero l’oggetto User dal db        
        //Recupero i ticket dell’utente
        List<Ticket>listaTicket = ticketRepository.findByUser_Username(username);//listaTicket= ticketRepository.findByUser;
        //Se esistono ticket con stato "NEW" o "IN_PROGRESS", l’utente non può modificare lo stato 
        boolean canModifyStatus=true;
        for(Ticket t : listaTicket){
            if(t.getTicketStatus().equals("NEW") || t.getTicketStatus().equals("IN_PROGRESS")){
                canModifyStatus=false;
            }
        }
        //Se può switcho lo stato dell'operatore da TRUE a FALSE e viceversa 
        if(canModifyStatus){
            if(user.getOperatorStatus()){
                user.setOperatorStatus(false);
            }else{
                user.setOperatorStatus(true);
            }
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successStatusMessage", "Stato modificato con successo!");
        }else{
            redirectAttributes.addFlashAttribute("errorStatusMessage", "Sono presenti ticket da terminare!");
        }
        return "redirect:/ticket";
    }
    
}