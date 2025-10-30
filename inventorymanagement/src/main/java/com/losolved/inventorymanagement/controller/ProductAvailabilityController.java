package com.losolved.inventorymanagement.controllers;

import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import com.losolved.inventorymanagement.services.ProductAvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class ProductAvailabilityController {
    private final ProductAvailabilityService availabilityService;

    public ProductAvailabilityController(ProductAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }


      @GetMapping("/product/{productId}")
    public ResponseEntity<Integer> calculateForProduct(@PathVariable Long productId) {
        Integer available = availabilityService.calculateAvailableUnits(productId);
        return available == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(available);
    }
}