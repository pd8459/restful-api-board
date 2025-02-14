package com.example.board.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<ItemResponseDto> addItem(@Valid @RequestBody ItemRequestDto requestDto) {
        Item savedItem = itemService.addItem(requestDto);
        return new ResponseEntity<>(new ItemResponseDto(savedItem), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItem() {
        List<Item> items = itemService.getAllItmes();
        List<ItemResponseDto> responseDtos = items.stream()
                .map(ItemResponseDto::new)
                .toList();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
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
