package it.lessons.spring.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.spring.ticket_platform.model.Note;

public interface NoteRepository extends JpaRepository<Note, Integer>{

}