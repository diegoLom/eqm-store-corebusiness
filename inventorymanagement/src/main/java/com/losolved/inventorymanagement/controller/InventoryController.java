package com.losolved.inventorymanagement.controller;



import com.losolved.inventorymanagement.dto.ProductComposition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @PostMapping("/products/{productId}/composition")
    public void setProductComposition(@PathVariable String productId,
                                      @RequestBody List<ProductComponentMapping> mappings) {
        // Define how inventory items compose a product
    }

    @GetMapping("/products/{productId}/composition")
    public ResponseEntity<ProductComposition> getProductComposition(@PathVariable String productId) {
        // Return complete product composition with pricing

        return null;
    }

    @GetMapping("/products/{productId}/availability")
    public ResponseEntity<ProductAvailability> checkAvailability(@PathVariable String productId,
                                                 @RequestParam Integer quantity) {
        // Check if enough inventory exists for all components
    }

    /**

    @PostMapping("/products/{productId}/reserve")
    public ReservationResult reserveInventory(@PathVariable String productId,
                                              @RequestParam Integer quantity) {
        // Reserve inventory for order
    }
**/


}