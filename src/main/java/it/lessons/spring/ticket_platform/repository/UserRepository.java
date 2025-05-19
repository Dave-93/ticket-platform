package it.lessons.spring.ticket_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.spring.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    //Cerca un utente in base al suo username
    public Optional<User> findByUsername(String username);
    
    //Restituisce tutti gli OPERATORI che hanno stato Disponibile
    List<User> findByOperatorStatusTrueAndRoles_Name(String roleName);

}