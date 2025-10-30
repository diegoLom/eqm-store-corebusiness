package com.losolved.inventorymanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_inventory_mapping")
@Data
public class ProductInventoryMapping {
    @Id
    private Integer mappingId;

    private Integer productId; // Reference to product in Product service

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private InventoryItem inventoryItem; // Aggregation: replaces the previous itemId

    private String componentType; // e.g., "base_material", "color", "personalization"

    private Integer quantityRequired; // How many of this item needed per product

    private Integer displayOrder; // For UI ordering

    private boolean required; // Is this component mandatory?

    // private Map<String, String> configuration;  Additional configuration
}