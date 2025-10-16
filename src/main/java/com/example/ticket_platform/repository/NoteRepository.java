package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
