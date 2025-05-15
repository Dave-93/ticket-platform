package it.lessons.spring.ticket_platform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Role {
    //Attributi
    @Id
    private Integer id;
    
    @NotNull
    private String name;
    
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
}