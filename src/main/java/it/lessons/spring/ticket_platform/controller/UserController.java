package it.lessons.spring.ticket_platform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.spring.ticket_platform.model.User;
import it.lessons.spring.ticket_platform.service.UserService;


@Controller
public class UserController {

    private final UserService userService;
    
    //@Autowired//se una classe ha un solo costruttore, Spring lo usa automaticamente per l'iniezione dei bean, anche senza l’annotazione @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/modificaStato")
    public String modificaStato(Authentication authentication, Model model, RedirectAttributes redirectAttributes){
        String username = authentication.getName();//Recupero lo username dell'utente loggato
        //Recupero i ticket dell’utente
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorStatusMessage", "Utente non trovato");
            return "redirect:/ticket";
        }
        //Se può switcho lo stato dell'operatore da TRUE a FALSE e viceversa 
        if (userService.canModifyStatus(username)) {
            boolean newStatus = userService.toggleOperatorStatus(user);
            redirectAttributes.addFlashAttribute("successStatusMessage",
                    newStatus ? "Stato modificato: ora sei disponibile" : "Stato modificato: ora sei non disponibile");
        }else{
            redirectAttributes.addFlashAttribute("errorStatusMessage", "Sono presenti ticket da terminare!");
        }
        return "redirect:/ticket";
    }
    
}