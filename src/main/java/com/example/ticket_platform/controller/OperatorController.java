package com.example.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ticket_platform.model.Note;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.NoteRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.security.DatabaseUserDetails;





@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    //lista ticket

    @GetMapping("/tickets")
    public String listOperatorTickets(@AuthenticationPrincipal DatabaseUserDetails userDetails, Model model) {
        User operator = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Ticket> tickets = ticketRepository.findByOperator(operator);
        model.addAttribute("tickets", tickets);
        return "operator/tickets";
    }
    
    @GetMapping("/tickets/{id}")
    public String ticketDetail(@PathVariable Long id, @AuthenticationPrincipal DatabaseUserDetails userDetails, Model model) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();


        model.addAttribute("ticket", ticket);
        model.addAttribute("statuses", Ticket.Status.values());
        return "operator/ticket-detail";
    }
    
    @PostMapping("/tickets/{id}/status")
    public String updateTicketStatus(@PathVariable Long id, @RequestParam Ticket.Status status, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        User operator = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        
        ticket.setStatus(status);
        ticketRepository.save(ticket);

        return "redirect:/operator/tickets/" + id;
    }
    
    @PostMapping("/tickets/{id}/notes")
    public String addNote(@PathVariable Long id, @RequestParam String content, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        User operator = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        
        Note note = new Note();
        note.setTicket(ticket);
        note.setAuthor(operator);
        note.setContent(content);
        noteRepository.save(note);

        return "redirect:/operator/tickets/" + id;
    }
    
    @GetMapping("/profile")
    public String operatorProfile(@AuthenticationPrincipal DatabaseUserDetails userDetails, Model model) {
        User operator = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("operator", operator);
        return "operator/profile";
    }
}
