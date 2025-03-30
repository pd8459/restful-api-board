package com.example.board.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Item addItem(ItemRequestDto requestDto) {
        Item item = new Item();
        item.setName(requestDto.getName());
        item.setDescription(requestDto.getDescription());
        item.setPrice(requestDto.getPrice());
        item.setStock(requestDto.getStock());
        item.setCategory(requestDto.getCategory());
        item.setImageUrl(requestDto.getImageUrl());

        return itemRepository.save(item);
    }


    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("아이템을 찾을 수 없습니다: " + id));
    }


    public Item updateItem(Long id, ItemRequestDto requestDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다"));
        item.setName(requestDto.getName());
        item.setDescription(requestDto.getDescription());
        item.setPrice(requestDto.getPrice());
        item.setStock(requestDto.getStock());;

        return itemRepository.save(item);
    }

    public boolean deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("아이템을 찾을 수 없습니다"));
        itemRepository.delete(item);
        return true;
    }
}
