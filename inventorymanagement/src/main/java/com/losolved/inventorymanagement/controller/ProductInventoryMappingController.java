package com.losolved.inventorymanagement.controllers;

import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import com.losolved.inventorymanagement.services.ProductInventoryMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mappings")
public class ProductInventoryMappingController {
    private final ProductInventoryMappingService mappingService;

    public ProductInventoryMappingController(ProductInventoryMappingService mappingService) {
        this.mappingService = mappingService;
    }

    @GetMapping
    public ResponseEntity<List<ProductInventoryMapping>> getAll() {
        return ResponseEntity.ok(mappingService.getAllMappings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductInventoryMapping> getById(@PathVariable Long id) {
        return mappingService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductInventoryMapping> create(@RequestBody ProductInventoryMapping mapping) {
        ProductInventoryMapping saved = mappingService.saveMapping(mapping);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mappingService.deleteMapping(id);
        return ResponseEntity.noContent().build();
    }
}