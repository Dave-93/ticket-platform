package it.lessons.spring.ticket_platform.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class User {
    //Attributi
    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    private String mail;

    @NotNull
    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean operatorStatus = true;
    /*
     * il campo non può essere null
     * il DB deve creare la colonna con valore predefinito true
     * e lato Java, il valore iniziale è true
     */

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    
    @OneToMany(mappedBy = "user")
    private List<Note>note;

    @OneToMany(mappedBy = "user")
    private List<Ticket>ticket;

    //Getter e Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(boolean operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Note> getNote() {
        return note;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }

    public List<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(List<Ticket> ticket) {
        this.ticket = ticket;
    }
}