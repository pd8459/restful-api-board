package com.example.board.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/add")
    public ResponseEntity<ItemResponseDto> addItem(@Valid @RequestBody ItemRequestDto requestDto) {
        Item savedItem = itemService.addItem(requestDto);
        return new ResponseEntity<>(new ItemResponseDto(savedItem), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Item>> getItems(@RequestParam String category, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Item> items;

        if ("women".equals(category)) {
            items = itemRepository.findByCategory("women", pageable);
        } else if ("men".equals(category)) {
            items = itemRepository.findByCategory("men", pageable);
        } else {
            items = itemRepository.findAll(pageable);
        }

        return ResponseEntity.ok(items);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id){
        Item item = itemService.getItemById(id);
        return new ResponseEntity<>(new ItemResponseDto(item), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long id, @RequestBody ItemRequestDto requestDto) {
        Item updatedItem = itemService.updateItem(id,requestDto);
        return ResponseEntity.ok(new ItemResponseDto(updatedItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemResponseDto> deleteItem(@PathVariable Long id) {
        if(itemService.deleteItem(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
