package com.example.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTicketId(Long ticketId);
}
