package com.losolved.inventorymanagement.services;

import com.losolved.inventorymanagement.dto.ProductAvailability;
import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ProductAvailabilityService {
    private final ProductInventoryMappingService mappingService;

    public ProductAvailabilityService(ProductInventoryMappingService mappingService) {
        this.mappingService = mappingService;
    }

    public boolean checkAvailability(Integer productId) {
        List<ProductInventoryMapping> mappings = mappingService.findByProductId(productId);

        long itemsRequiredQuantity = mappings.stream().filter(ProductInventoryMapping::isRequired).count();
        long itemRequiredWithEnoughResources = mappings.stream()
                .filter(hasSufficientQuantityFromMapping())
                .count();

        return itemRequiredWithEnoughResources >= itemsRequiredQuantity;
    }

    public ProductAvailability getProductAvailability(Integer productId) {
        boolean isAvailable = checkAvailability(productId);
        Integer availableUnits = calculateAvailableUnits(productId);
        return new ProductAvailability(isAvailable, availableUnits, productId);
    }

    public Integer calculateAvailableUnits(Integer productId) {
        List<ProductInventoryMapping> mappings = mappingService.findByProductId(productId);

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
            if (!m.isRequired()) return true;

            Integer required = m.getQuantityRequired();
            if (required == null || required <= 0) return true;

            if (m.getInventoryItem() == null || m.getInventoryItem().getStockQuantity() == null) return false;

            return m.getInventoryItem().getStockQuantity() >= required;
        };
    }
}