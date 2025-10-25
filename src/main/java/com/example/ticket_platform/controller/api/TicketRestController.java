package com.example.ticket_platform.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.repository.TicketRepository;


@RestController
@RequestMapping("api/tickets")
public class TicketRestController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> list(@RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false) Ticket.Status status) {

        if (categoryId != null && status != null) {
            return ticketRepository.findByCategoryIdAndStatus(categoryId, status);
        } else if (categoryId != null) {
            return ticketRepository.findByCategoryId(categoryId);
        } else if (status != null) {
            return ticketRepository.findByStatus(status);
        } else {
            return ticketRepository.findAll();
        }
    }
    
    @GetMapping("/{id}")
    public Ticket getOne(@PathVariable Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }
}
