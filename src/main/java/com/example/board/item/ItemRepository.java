package com.example.board.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByCategory(String category, Pageable pageable);
}
