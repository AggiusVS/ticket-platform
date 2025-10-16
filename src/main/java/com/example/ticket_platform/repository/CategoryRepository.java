package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
