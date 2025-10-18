package com.losolved.inventorymanagement.services;

import com.losolved.inventorymanagement.model.InventoryItem;
import com.losolved.inventorymanagement.repositories.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InventoryItemService {
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> getAllItems() {
        return StreamSupport.stream(inventoryItemRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<InventoryItem> getItemById(Long id) {
        return inventoryItemRepository.findById(id);
    }

    public Optional<InventoryItem> getBySku(String sku) {
        return inventoryItemRepository.findBySku(sku);
    }

    public List<InventoryItem> searchByName(String name) {
        return inventoryItemRepository.findByNameContainingIgnoreCase(name);
    }

    public InventoryItem saveItem(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}