package it.lessons.spring.ticket_platform.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Ticket {
    //Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Titolo obbligatorio")
    @Column(nullable = false)
    private String title;

    @Column(length=1000)
    private String description;

    @NotBlank
    //todo: col Pattern Limito i valori ammessi a quelli stabiliti nel "regexp"
    @Pattern(regexp = "NEW|IN_PROGRESS|CLOSED"/*, message = "Stato non valido"*/)
    @Column(nullable = false, length = 15)
    private String ticketStatus;

    //todo occorre il campo data_creazione??

    @ManyToOne
    @NotNull(message = "Operatore obbligatorio")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @NotNull(message = "Categoria obbligatoria")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @OneToMany(mappedBy = "ticket")
    private List<Note>note;
    
    //Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Note> getNote() {
        return note;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}