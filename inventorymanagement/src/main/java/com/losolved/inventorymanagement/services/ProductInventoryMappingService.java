package com.losolved.inventorymanagement.services;

import com.losolved.inventorymanagement.dto.ProductAvailability;
import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import com.losolved.inventorymanagement.repositories.ProductInventoryMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductInventoryMappingService {
    private final ProductInventoryMappingRepository mappingRepository;
    private final InventoryItemService inventoryItemService;

    public ProductInventoryMappingService(ProductInventoryMappingRepository mappingRepository, InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
        this.mappingRepository = mappingRepository;
    }

    public List<ProductInventoryMapping> getAllMappings() {
        return StreamSupport.stream(mappingRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<ProductInventoryMapping> getMappingById(Long id) {
        return mappingRepository.findById(id);
    }

    public List<ProductInventoryMapping> findByProductId(Long productId) {
        return mappingRepository.findByProductId(productId);
    }

    public Optional<ProductInventoryMapping> findByInventoryItemId(Long inventoryItemId) {
        return mappingRepository.findByInventoryItemId(inventoryItemId);
    }

    public ProductInventoryMapping saveMapping(ProductInventoryMapping mapping) {
        return mappingRepository.save(mapping);
    }

    public void deleteMapping(Long id) {
        mappingRepository.deleteById(id);
    }



    //TODO: Remove below methods to a dedicated ProductAvailabilityService
        public boolean checkAvailability(Long productId) {
        List<ProductInventoryMapping> mappings = findByProductId(productId);

        long itemsRequiredQuantity = mappings.stream().filter(x -> x.isRequired()).count();
        long itemRequiredWithEnoughResources = mappings.stream().filter(x -> hasSufficientQuantityFromMapping().test(x)).count();

        boolean isAvailable = itemRequiredWithEnoughResources >= itemsRequiredQuantity;

        return isAvailable;
    }

    public ProductAvailability getProductAvailability(Long productId) {


        boolean isAvailable = checkAvailability(productId);


    Integer availableUnits = calculateAvailableUnits(productId);

        return new

    ProductAvailability(isAvailable, availableUnits);
}

    public Integer calculateAvailableUnits(Long productId) {
        List<ProductInventoryMapping> mappings = findByProductId(productId);

        return mappings.stream()
                .map(getAvailableUnitsFunction())
                .filter(u -> u != Integer.MAX_VALUE) // ignore non-constraining components
                .min(Integer::compareTo)
                .orElse(0);
    }

    public static Function<ProductInventoryMapping, Integer> getAvailableUnitsFunction() {
        return m -> {
            if (m == null || !m.isRequired()) return Integer.MAX_VALUE;

            Integer stockQty = m.getInventoryItem() != null ? m.getInventoryItem().getStockQuantity() : 0;
            Integer qtyRequired = m.getQuantityRequired() != null ? m.getQuantityRequired() : 0;

            if (qtyRequired <= 0) return Integer.MAX_VALUE;

            return stockQty / qtyRequired;
        };
    }



    public static Predicate<ProductInventoryMapping> hasSufficientQuantityFromMapping() {
        return m -> {
            if (m == null) return false;
            // If the component is not required, it's considered satisfied.
            if (!m.isRequired()) return true;

            Integer required = m.getQuantityRequired();
            // No positive requirement means no minimum to check.
            if (required == null || required <= 0) return true;

            // Available quantity is taken from the aggregated InventoryItem.
            if (m.getInventoryItem() == null || m.getInventoryItem().getStockQuantity() == null) return false;

            return m.getInventoryItem().getStockQuantity() >= required;
        };
    }
}
