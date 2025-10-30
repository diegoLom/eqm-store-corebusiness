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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void calculateAvailableUnits_emptyMappings_returnsZero() {
        when(mappingRepository.findByProductId(1L)).thenReturn(Collections.emptyList());
        assertEquals(0, service.calculateAvailableUnits(1L));
    }

    @Test
    void calculateAvailableUnits_nonRequiredOnly_returnsZero() {
        ProductInventoryMapping m = mock(ProductInventoryMapping.class);
        when(m.isRequired()).thenReturn(false);

        when(mappingRepository.findByProductId(1L)).thenReturn(Collections.singletonList(m));
        assertEquals(0, service.calculateAvailableUnits(1L));
    }

    @Test
    void calculateAvailableUnits_multipleMappings_returnsMinimum() {
        ProductInventoryMapping m1 = mock(ProductInventoryMapping.class);
        InventoryItem i1 = mock(InventoryItem.class);
        when(m1.isRequired()).thenReturn(true);
        when(m1.getInventoryItem()).thenReturn(i1);
        when(i1.getStockQuantity()).thenReturn(10);
        when(m1.getQuantityRequired()).thenReturn(2); // 10/2 = 5

        ProductInventoryMapping m2 = mock(ProductInventoryMapping.class);
        InventoryItem i2 = mock(InventoryItem.class);
        when(m2.isRequired()).thenReturn(true);
        when(m2.getInventoryItem()).thenReturn(i2);
        when(i2.getStockQuantity()).thenReturn(7);
        when(m2.getQuantityRequired()).thenReturn(2); // 7/2 = 3

        ProductInventoryMapping m3 = mock(ProductInventoryMapping.class);
        InventoryItem i3 = mock(InventoryItem.class);
        when(m3.isRequired()).thenReturn(true);
        when(m3.getInventoryItem()).thenReturn(i3);
        when(i3.getStockQuantity()).thenReturn(20);
        when(m3.getQuantityRequired()).thenReturn(5); // 20/5 = 4

        List<ProductInventoryMapping> list = Arrays.asList(m1, m2, m3);
        when(mappingRepository.findByProductId(42L)).thenReturn(list);

        // expected minimum = 3
        assertEquals(3, service.calculateAvailableUnits(42L));
    }

    @Test
    void calculateAvailableUnits_nonPositiveRequiredIgnored() {
        ProductInventoryMapping m = mock(ProductInventoryMapping.class);
        InventoryItem i = mock(InventoryItem.class);
        when(m.isRequired()).thenReturn(true);
        when(m.getInventoryItem()).thenReturn(i);
        when(i.getStockQuantity()).thenReturn(50);
        when(m.getQuantityRequired()).thenReturn(0); // treated as non-constraining

        when(mappingRepository.findByProductId(5L)).thenReturn(Collections.singletonList(m));
        assertEquals(0, service.calculateAvailableUnits(5L));
    }
}
