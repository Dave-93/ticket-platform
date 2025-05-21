package it.lessons.spring.ticket_platform.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.lessons.spring.ticket_platform.model.Ticket;
import it.lessons.spring.ticket_platform.service.TicketService;


@RestController
@RequestMapping("/api/ticket")
public class TicketRestController {

    private final TicketService ticketService;

    public TicketRestController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    //localhost:8080/api/ticket
    @GetMapping
    public ResponseEntity<List<Ticket>> ticketList() {
        List<Ticket> listTickets = ticketService.getAllTickets();
        if (listTickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204 se lista vuota
        }
        return new ResponseEntity<>(listTickets, HttpStatus.OK); //200 se ci sono ticket
    }
    
    //localhost:8080/api/ticket/category/{id}
    @GetMapping("/category/{id}")
    public ResponseEntity<List<Ticket>> findByCategory(@PathVariable Integer id){
        List<Ticket> listCategory = ticketService.findTicketByCategory(id);
        if(listCategory.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listCategory, HttpStatus.OK);
    }
    
    //localhost:8080/api/ticket/status/{ticketStatus}
    @GetMapping("/status/{ticketStatus}")
    public ResponseEntity<List<Ticket>> findByStatus(@PathVariable String ticketStatus){
        List<Ticket> listStatus = ticketService.findTicketByStatus(ticketStatus);
        if(listStatus.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listStatus, HttpStatus.OK);
    }
}