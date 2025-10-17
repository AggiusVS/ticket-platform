package com.example.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.repository.NoteRepository;
import com.example.ticket_platform.repository.TicketRepository;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/tickets")
    public String listTickets(@RequestParam(required=false) String keyword, Model model) {
        List<Ticket> tickets;
        if(keyword != null && !keyword.isEmpty()) {
            tickets = ticketRepository.findByTitleContainingIgnoreCase(keyword);
        } else { 
            tickets = ticketRepository.findAll();
        }
        model.addAttribute("tickets", tickets);
        model.addAttribute("keyword", keyword);
        return "admin/tickets";
    }

    @GetMapping("/tickets/{id}")
    public String ticketDetail(@PathVariable Long id, Model model) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        model.addAttribute("ticket", ticket);
        return "admin/ticket-detail";
    }
    
}
