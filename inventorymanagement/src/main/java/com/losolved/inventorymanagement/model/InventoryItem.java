package com.losolved.inventorymanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name ="inventory_item")
@Data
public class InventoryItem {

    @Id
    private Integer itemId;
    private String itemType; // e.g., "material", "component", "finishing"
    private String name;
    private String description;
    private String sku;
    private Integer stockQuantity;
    private BigDecimal unitPrice;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}