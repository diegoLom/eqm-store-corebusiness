package com.losolved.product.service;

import com.losolved.product.service.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "inventory-service")
public interface InventoryServiceClient {

    @GetMapping("/api/inventory/{productId}")
    InventoryResponse checkInventory(@PathVariable String productId);

    @PutMapping("/api/inventory/{productId}/deduct")
    void deductInventory(@PathVariable String productId, @RequestBody InventoryDeductRequest request);
}

