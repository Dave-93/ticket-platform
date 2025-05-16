package it.lessons.spring.ticket_platform.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Category {
    //Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message="Tipologia obbligatoria")
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
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

    public List<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(List<Ticket> ticket) {
        this.ticket = ticket;
    }
}
/*  POPOLAZIONE MANUALE DEL DB
use db_platform;  

show columns from category;
insert into category (name) values ('Installazione Software'), ('Aggiornamento Software'), ('Problema Hardware'), ('Connessione Rete'), ('Nuova Postazione'), ('Stampanti e Scanner'), ('Telefono VoIP');
select * from category; 

show columns from user;
ALTER TABLE user MODIFY operator_status TINYINT NOT NULL DEFAULT 1;
* ALTER TABLE user CHANGE name username VARCHAR(255) NOT NULL UNIQUE;
insert into user (id, name, mail, password) values (1, 'admin', 'administrator@support.it', '{noop}admin'), (2, 'operatore1','operatore1@support.it','{noop}operatore1'), (3, 'operatore2','operatore2@support.it','{noop}operatore2'), (4, 'operatore3','operatore3@support.it','{noop}operatore3');
select * from user;

show columns from role;
insert into role (id, name) values (1, 'ADMIN'), (2, 'OPERATOR');
select * from role;

show columns from user_roles;
insert into user_roles (user_id, roles_id) values (1,1), (2,2), (3,2), (4,2);
select * from user_roles;

select * from ticket;
*/