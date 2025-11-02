// java
package com.losolved.inventorymanagement.services;

import com.losolved.inventorymanagement.model.InventoryItem;
import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import com.losolved.inventorymanagement.repositories.ProductInventoryMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductInventoryMappingServiceTest {

    @Mock
    private ProductInventoryMappingRepository mappingRepository;

    @Mock
    private InventoryItemService inventoryItemService;

    private ProductInventoryMappingService service;

    @BeforeEach
    void setUp() {
        service = new ProductInventoryMappingService(mappingRepository, inventoryItemService);
    }

    @Test
    void getAllMappings_returnsAll() {
        ProductInventoryMapping m1 = ProductInventoryMapping.builder()
                .mappingId(1)
                .productId(100)
                .build();
        ProductInventoryMapping m2 = ProductInventoryMapping.builder()
                .mappingId(2)
                .productId(200)
                .build();

        when(mappingRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<ProductInventoryMapping> result = service.getAllMappings();

        assertEquals(2, result.size());
        assertEquals(m1, result.get(0));
        assertEquals(m2, result.get(1));
    }

    @Test
    void getMappingById_found() {
        ProductInventoryMapping m = ProductInventoryMapping.builder()
                .mappingId(1)
                .productId(100)
                .build();

        when(mappingRepository.findById(1L)).thenReturn(Optional.of(m));

        Optional<ProductInventoryMapping> result = service.getMappingById(1L);

        assertTrue(result.isPresent());
        assertEquals(m, result.get());
    }

    @Test
    void getMappingById_notFound() {
        when(mappingRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ProductInventoryMapping> result = service.getMappingById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByProductId_returnsList() {
        ProductInventoryMapping m = ProductInventoryMapping.builder()
                .mappingId(1)
                .productId(100)
                .build();

        when(mappingRepository.findByProductId(100)).thenReturn(Collections.singletonList(m));

        List<ProductInventoryMapping> result = service.findByProductId(100);

        assertEquals(1, result.size());
        assertEquals(m, result.get(0));
    }

    @Test
    void findByInventoryItemId_returnsOptional() {
        InventoryItem item = new InventoryItem();
        item.setItemId(10);

        ProductInventoryMapping m = ProductInventoryMapping.builder()
                .mappingId(1)
                .inventoryItem(item)
                .build();

        when(mappingRepository.findByInventoryItemItemId(10L)).thenReturn(Optional.of(m));

        Optional<ProductInventoryMapping> result = service.findByInventoryItemId(10L);

        assertTrue(result.isPresent());
        assertEquals(m, result.get());
    }

    @Test
    void saveMapping_delegatesToRepository() {
        ProductInventoryMapping m = ProductInventoryMapping.builder()
                .mappingId(1)
                .productId(100)
                .build();

        when(mappingRepository.save(m)).thenReturn(m);

        ProductInventoryMapping saved = service.saveMapping(m);

        assertEquals(m, saved);
        verify(mappingRepository, times(1)).save(m);
    }

    @Test
    void updateMapping_setsIdAndSaves() {
        Integer id = 1;
        ProductInventoryMapping existing = ProductInventoryMapping.builder()
                .mappingId(2)
                .productId(100)
                .build();

        ProductInventoryMapping toUpdate = ProductInventoryMapping.builder()
                .productId(200)
                .build();

        when(mappingRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(existing));
        ProductInventoryMapping savedMapping = ProductInventoryMapping.builder()
                .mappingId(id)
                .productId(200)
                .build();
        when(mappingRepository.save(any(ProductInventoryMapping.class))).thenReturn(savedMapping);

        Optional<ProductInventoryMapping> result = service.updateMapping(id, toUpdate);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getMappingId());
        assertEquals(savedMapping, result.get());
        verify(mappingRepository).findById(Long.valueOf(id));
        verify(mappingRepository).save(any(ProductInventoryMapping.class));
    }

    @Test
    void updateMapping_notFound_returnsEmpty() {
        Integer id = 99;
        ProductInventoryMapping toUpdate = ProductInventoryMapping.builder()
                .productId(200)
                .build();

        when(mappingRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());

        Optional<ProductInventoryMapping> result = service.updateMapping(id, toUpdate);

        assertFalse(result.isPresent());
        verify(mappingRepository).findById(Long.valueOf(id));
        verify(mappingRepository, never()).save(any(ProductInventoryMapping.class));
    }

    @Test
    void deleteMapping_callsRepositoryDelete() {
        Long id = 1L;

        doNothing().when(mappingRepository).deleteById(id);

        service.deleteMapping(id);

        verify(mappingRepository, times(1)).deleteById(id);
    }
}
