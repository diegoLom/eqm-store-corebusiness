package com.losolved.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    private Integer productId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String category;
    private boolean customizable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}