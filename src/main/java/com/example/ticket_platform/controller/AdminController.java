package com.example.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ticket_platform.model.Note;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.CategoryRepository;
import com.example.ticket_platform.repository.NoteRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.security.DatabaseUserDetails;

import jakarta.validation.Valid;




@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private CategoryRepository categoryRepository;

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
    
    @PostMapping("/tickets/{id}/notes")
    public String addNote(@PathVariable Long id, @RequestParam String content, @AuthenticationPrincipal DatabaseUserDetails userDetails, Model model) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        if (content == null || content.trim().isEmpty()) { 
            model.addAttribute("ticket", ticket);
            model.addAttribute("error", "Il contenuto della nota non può essere vuoto."); 
            return "admin/ticket-detail"; 
        }

        User author = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Note note = new Note();
        note.setTicket(ticket);
        note.setContent(content);
        note.setAuthor(author);
        noteRepository.save(note);
        return "redirect:/admin/tickets/" + id;
    }
    
    @GetMapping("/tickets/new")
    public String newTicketForm(Model model) {
        Ticket ticket = new Ticket();
        model.addAttribute("ticket", ticket);
        model.addAttribute("availableOperators", userRepository.findAll().stream()
                            .filter(u -> !u.isUnavailable() && u.getRole() == User.Role.OPERATOR).toList());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/ticket-form";
                            
    }
    
    @PostMapping("/tickets")
    public String saveNewTicket(@Valid @ModelAttribute Ticket ticket, BindingResult result, Model model) {
        if (result.hasErrors()) { 
            model.addAttribute("availableOperators", userRepository.findAll().stream()
                    .filter(u -> !u.isUnavailable() && u.getRole() == User.Role.OPERATOR)
                    .toList());
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/ticket-form";
        }

        ticketRepository.save(ticket);
        return "redirect:/admin/tickets";
    }
    
    @GetMapping("/tickets/{id}/edit")
    public String editTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        model.addAttribute("ticket", ticket);
        model.addAttribute("availableOperators", userRepository.findAll().stream()
                            .filter(u -> !u.isUnavailable() && u.getRole() == User.Role.OPERATOR).toList());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/ticket-form";
    }
    
    @PostMapping("/tickets/{id}")
    public String updateTicket(@PathVariable Long id, @Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("availableOperators", userRepository.findAll().stream()
                    .filter(u -> !u.isUnavailable() && u.getRole() == User.Role.OPERATOR)
                    .toList());
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/ticket-form";
        }

        ticket.setId(id);
        ticketRepository.save(ticket);
        return "redirect:/admin/tickets";
    }
    
}
