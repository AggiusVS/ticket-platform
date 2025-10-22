package com.example.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Category;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findByCategory(Category category);
    List<Ticket> findByStatus(Ticket.Status status);
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);
    List<Ticket> findByOperatorId(Long operatorId);
    List<Ticket> findByOperator(User operator);
    List<Ticket> findByOperatorAndStatusIn(User operator, List<Ticket.Status> statuses);
}
