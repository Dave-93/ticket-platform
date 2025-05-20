package it.lessons.spring.ticket_platform.controller;
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
import it.lessons.spring.ticket_platform.service.NoteService;
import it.lessons.spring.ticket_platform.service.UserService;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/ticket")
public class NoteController {
    
    private final NoteService noteService;
    private final UserService userService;
    //private final TicketRepository ticketRepository;

    public NoteController(NoteService noteService, UserService userService){
        this.noteService = noteService;
        this.userService = userService;
    }

    /*Creazione Note*/
    @GetMapping("/{id}/note")
    public String addNote(@PathVariable Integer id, Model model){
        Note addNote = noteService.createNoteForTicket(id);
        model.addAttribute("note", addNote);
        return "note/create";
    }
    @PostMapping("/note")
    public String newNote(@Valid @ModelAttribute("note") Note formNote, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Authentication authentication){
        if (bindingResult.hasErrors()){
            model.addAttribute("note", formNote);
            return "note/create";//Torna alla pagina con gli errori di validazione
        }
        //Ottiengo il nome utente dell'utente autenticato
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utente non trovato");
            return "redirect:/ticket";
        }
        //Salva la nota
        noteService.saveNote(formNote, user);
        redirectAttributes.addFlashAttribute("successMessage", "Nota aggiunta");
        return "redirect:/ticket/show/" + formNote.getTicket().getId();
    }
    
}