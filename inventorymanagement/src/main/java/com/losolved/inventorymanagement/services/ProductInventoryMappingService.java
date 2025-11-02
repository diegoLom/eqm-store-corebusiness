package com.losolved.inventorymanagement.services;

import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import com.losolved.inventorymanagement.repositories.ProductInventoryMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductInventoryMappingService {
    private final ProductInventoryMappingRepository mappingRepository;
    private final InventoryItemService inventoryItemService;

    public ProductInventoryMappingService(ProductInventoryMappingRepository mappingRepository,
                                          InventoryItemService inventoryItemService) {
        this.mappingRepository = mappingRepository;
        this.inventoryItemService = inventoryItemService;
    }

    public List<ProductInventoryMapping> getAllMappings() {
        return StreamSupport.stream(mappingRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<ProductInventoryMapping> getMappingById(Long id) {
        return mappingRepository.findById(id);
    }

    public List<ProductInventoryMapping> findByProductId(Integer productId) {
        return mappingRepository.findByProductId(productId);
    }

    public Optional<ProductInventoryMapping> findByInventoryItemId(Long inventoryItemId) {
        return mappingRepository.findByInventoryItemItemId(inventoryItemId);
    }

    public ProductInventoryMapping saveMapping(ProductInventoryMapping mapping) {
        return mappingRepository.save(mapping);
    }

    public Optional<ProductInventoryMapping> updateMapping(Integer id, ProductInventoryMapping mapping) {
        mapping.setMappingId(id);

        return mappingRepository.findById(Long.valueOf(id))
                .map(existing -> mappingRepository.save(mapping));
    }

    public void deleteMapping(Long id) {
        mappingRepository.deleteById(id);
    }
}