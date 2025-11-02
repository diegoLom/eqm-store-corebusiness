package com.losolved.inventorymanagement.controller;

import com.losolved.inventorymanagement.dto.ProductAvailability;
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
    public ResponseEntity<ProductAvailability> calculateForProduct(@PathVariable Integer productId) {
        ProductAvailability available = availabilityService.getProductAvailability(productId);
        return  !available.isAvailable()? ResponseEntity.notFound().build() : ResponseEntity.ok(available);
    }
}