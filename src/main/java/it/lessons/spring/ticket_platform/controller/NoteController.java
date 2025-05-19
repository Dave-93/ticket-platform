package it.lessons.spring.ticket_platform.controller;
import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.spring.ticket_platform.model.Note;
import it.lessons.spring.ticket_platform.model.User;
import it.lessons.spring.ticket_platform.repository.NoteRepository;
import it.lessons.spring.ticket_platform.repository.TicketRepository;
import it.lessons.spring.ticket_platform.repository.UserRepository;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/ticket")
public class NoteController {
    
    private final NoteRepository noteRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    /*Creazione Note*/
    @GetMapping("/{id}/note")
    public String addNote(@PathVariable Integer id, Model model){
        Note addNote = new Note();
        addNote.setTicket(ticketRepository.findById(id).get());
        
        //Imposta la data automaticamente alla creazione
        addNote.setNoteDate(LocalDate.now());

        model.addAttribute("note", addNote);
        return "note/create";
    }
    @PostMapping("/note")
    public String newNote(@Valid @ModelAttribute("note") Note formNote, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Authentication authentication){
        if (bindingResult.hasErrors()) {
            // Riaggancia il Ticket prima di ritornare alla view (se serve)
            Integer id = formNote.getTicket().getId();
            formNote.setTicket(ticketRepository.findById(id).orElse(null));
            model.addAttribute("note", formNote);
            return "note/create";  // Torna alla pagina con gli errori di validazione
        }
        //Ottiengo il nome utente dell'utente autenticato
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        formNote.setUser(user);
        //Imposta automaticamente la data odierna
        formNote.setNoteDate(LocalDate.now());
        //Salva la nota
        noteRepository.save(formNote);
        redirectAttributes.addFlashAttribute("successMessage", "Nota aggiunta");
        return "redirect:/ticket/show/" + formNote.getTicket().getId();
    }
    
}