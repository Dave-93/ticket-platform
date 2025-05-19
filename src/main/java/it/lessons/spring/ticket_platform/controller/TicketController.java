package it.lessons.spring.ticket_platform.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.spring.ticket_platform.model.Ticket;
import it.lessons.spring.ticket_platform.model.User;
import it.lessons.spring.ticket_platform.repository.CategoryRepository;
import it.lessons.spring.ticket_platform.repository.TicketRepository;
import it.lessons.spring.ticket_platform.repository.UserRepository;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository, CategoryRepository categoryRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String index(Authentication authentication, Model model) {
        String username = authentication.getName();//Recupero lo username dell’utente loggato
        List<Ticket>listaTicket = new ArrayList<>();//Creo la lista dei ticket da mostrare.
        if("admin".equals(username)){
            listaTicket = ticketRepository.findAll();//L’admin vede tutti i ticket
        }else{
            listaTicket = ticketRepository.findByUser_Username(username);//Gli operatori vedono solo i propri
        }
        //Cerca nel db un utente con username e se lo trova prende lo stato o non fa nulla 
        User user = userRepository.findByUsername(username).orElse(null);
        if(user != null){
            model.addAttribute("operatorStatus", user.getOperatorStatus());
        }
        model.addAttribute("list", listaTicket);
        model.addAttribute("name", authentication.getName());
        return "index";
    }

    /*Ricerca Ticket per titolo*/
    @GetMapping("/search")
    public String search(Model model, @RequestParam(name = "keyword", required = false) String title) {
        List<Ticket> tickets;
        if (title != null && !title.isBlank()) {
            tickets = ticketRepository.findByTitleContainingIgnoreCase(title);
        } else {
            tickets = ticketRepository.findAll();
        }
        model.addAttribute("list", tickets);
        model.addAttribute("keyword", title);
        return "index";
    }

    /*Creazione Ticket*/
    @GetMapping("/create")
    public String addTicket(Model model) {
        Ticket newTicket = new Ticket();//Inizializzo l'oggetto
        model.addAttribute("ticket", newTicket);
        newTicket.setTicketStatus("NEW");//Imposto la selezione del radio di default
        model.addAttribute("categories", categoryRepository.findAll());//visualizzo la lista
        model.addAttribute("users", userRepository.findByOperatorStatusTrueAndRoles_Name("OPERATOR"));//todo visualizzo gli user online
        return "ticket/create";
    }
    @PostMapping("/create")
    public String newTicket(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryRepository.findAll());//Ripopola la lista categorie nel model
            model.addAttribute("users", userRepository.findByOperatorStatusTrueAndRoles_Name("OPERATOR"));//Ripopola la lista degli operatori nel model
            return "ticket/create";
        }
        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket creato");
        return "redirect:/ticket";
    }

    /*Dettaglio ticket*/
    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Optional<Ticket> optTicket =  ticketRepository.findById(id);
        if(optTicket.isPresent()){
            model.addAttribute("ticket", ticketRepository.findById(id).get());
            return "/ticket/show";
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket non presente");
            return "redirect:/ticket";
        }
    }
    
    /*Modifica Ticket*/
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        model.addAttribute("ticket", ticketRepository.findById(id).get());
        model.addAttribute("categories", categoryRepository.findAll());//visualizzo la lista
        return "/ticket/edit";
    }
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id,@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes){
        Optional<Ticket> oldTicket = ticketRepository.findById(id);
        if (oldTicket.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket non trovato");
            return "redirect:/ticket";
        }
        //Recupero il ticket esistente per copiare l'user(che non deve essere cambiato in modifica)
        Ticket copyUser = oldTicket.get();
        formTicket.setUser(copyUser.getUser());//riprende l'user esistente
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("users", userRepository.findAll()); 
            return "/ticket/edit";
        }
        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Modifica effettuata con successo");
        return "redirect:/ticket";
    }

    /*Elimina Ticket*/
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        ticketRepository.deleteById(id);  
        redirectAttributes.addFlashAttribute("successMessage", "Ticket eliminato");
        return "redirect:/ticket";
    }
    
} 