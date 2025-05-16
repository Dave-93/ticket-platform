package it.lessons.spring.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.lessons.spring.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") String roleName);
    //Restituisce tutti gli USER che hanno come ruolo il PARAMETRO passato
}