package com.losolved.inventorymanagement.repositories;

import com.losolved.inventorymanagement.model.InventoryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySku(String sku);
    List<InventoryItem> findByNameContainingIgnoreCase(String name);

}