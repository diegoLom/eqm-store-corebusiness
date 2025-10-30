package com.losolved.inventorymanagement.repositories;

import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInventoryMappingRepository extends CrudRepository<ProductInventoryMapping, Long> {
    List<ProductInventoryMapping> findByProductId(Long productId);
    Optional<ProductInventoryMapping> findByInventoryItemId(Long inventoryItemId);
}