package com.losolved.inventorymanagement.repositories;

import com.losolved.inventorymanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}