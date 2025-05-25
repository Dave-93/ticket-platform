package it.lessons.spring.ticket_platform.controller;

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
import it.lessons.spring.ticket_platform.service.TicketService;
import it.lessons.spring.ticket_platform.service.UserService;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public TicketController(TicketService ticketService, UserService userService, CategoryRepository categoryRepository){
        this.ticketService = ticketService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String index(Authentication authentication, Model model) {
        String username = authentication.getName();//Recupero lo username dell’utente loggato
        List<Ticket> listaTicket = "admin".equals(username)
            ? ticketService.getAllTickets()//L’admin vede tutti i ticket
            : ticketService.getTicketsByUser(username);//Gli operatori vedono solo i propri
        //Cerca nel db un utente con username e se lo trova prende lo stato o non fa nulla 
        User user = userService.findByUsername(username).orElse(null);
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
        List<Ticket> tickets = ticketService.searchTicketsByTitle(title);
        model.addAttribute("list", tickets);
        model.addAttribute("keyword", title);
        return "index";
    }

    /*Creazione Ticket*/
    @GetMapping("/create")
    public String addTicket(Model model, RedirectAttributes redirectAttributes) {
        List<User> operatoriDisponibili = userService.findAvailableOperators();
        //Controllo su operatori disponibili
        if (operatoriDisponibili.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nessun operatore disponibile! Impossibile creare un nuovo ticket.");
            return "redirect:/ticket";
        }

        Ticket newTicket = new Ticket();//Inizializzo l'oggetto
        model.addAttribute("ticket", newTicket);
        newTicket.setTicketStatus("NEW");//Imposto la selezione del radio di default
        model.addAttribute("categories", categoryRepository.findAll());//visualizzo la lista
        model.addAttribute("users", operatoriDisponibili);//visualizzo gli operatori disponibili
        return "ticket/create";
    }
    @PostMapping("/create")
    public String newTicket(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryRepository.findAll());//Ripopola la lista categorie nel model
            model.addAttribute("users", userService.findAvailableOperators());//Ripopola la lista con gli operatori disponibili
            return "ticket/create";
        }
        ticketService.saveTicket(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket creato");
        return "redirect:/ticket";
    }

    /*Dettaglio ticket*/
    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Optional<Ticket> optTicket =  ticketService.findTicketById(id);
        if(optTicket.isPresent()){
            model.addAttribute("ticket", optTicket.get());
            return "/ticket/show";
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket non presente");
            return "redirect:/ticket";
        }
    }
    
    /*Modifica Ticket*/
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Optional<Ticket> optTicket = ticketService.findTicketById(id);
        if (optTicket.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket non trovato");
            return "redirect:/ticket";
        }
        model.addAttribute("ticket", optTicket.get());
        model.addAttribute("categories", categoryRepository.findAll());//visualizzo la lista
        return "/ticket/edit";
    }
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id,@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes){
        Optional<Ticket> oldTicket = ticketService.findTicketById(id);
        if (oldTicket.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket non trovato");
            return "redirect:/ticket";
        }
        formTicket.setUser(oldTicket.get().getUser());//riprende l'user esistente
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "/ticket/edit";
        }
        ticketService.saveTicket(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Modifica effettuata con successo");
        return "redirect:/ticket";
    }

    /*Elimina Ticket*/
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        ticketService.deleteTicket(id);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket eliminato");
        return "redirect:/ticket";
    }
    
} 