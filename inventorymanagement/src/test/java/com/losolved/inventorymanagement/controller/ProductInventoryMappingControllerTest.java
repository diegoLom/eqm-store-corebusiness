// java
package com.losolved.inventorymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losolved.inventorymanagement.model.ProductInventoryMapping;
import com.losolved.inventorymanagement.services.ProductInventoryMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductInventoryMappingControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private ProductInventoryMappingService mappingService;

    @InjectMocks
    private ProductInventoryMappingController mappingController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mappingController).build();
    }

    private ProductInventoryMapping sampleMapping() {
        return ProductInventoryMapping.builder()
                .productId(1)
                .mappingId(10)
                .quantityRequired(10)
                .build();
    }

    @Test
    void getAll_returnsList() throws Exception {
        ProductInventoryMapping m = sampleMapping();
        when(mappingService.getAllMappings()).thenReturn(Collections.singletonList(m));

        mockMvc.perform(get("/api/mappings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].mappingId", is(10)));

        verify(mappingService, times(1)).getAllMappings();
    }

    @Test
    void getById_found() throws Exception {
        ProductInventoryMapping m = sampleMapping();
        when(mappingService.getMappingById(10L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/api/mappings/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mappingId", is(10)))
                .andExpect(jsonPath("$.productId", is(1)));

        verify(mappingService).getMappingById(10L);
    }

    @Test
    void getById_notFound() throws Exception {
        when(mappingService.getMappingById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/mappings/10"))
                .andExpect(status().isNotFound());

        verify(mappingService).getMappingById(10L);
    }

    @Test
    void create_returnsSaved() throws Exception {
        ProductInventoryMapping toSave = sampleMapping();
        ProductInventoryMapping toSaveClone = ProductInventoryMapping.builder()
                .productId(toSave.getProductId())
                .quantityRequired(toSave.getQuantityRequired())
                .build();

        ProductInventoryMapping saved = sampleMapping();
        when(mappingService.saveMapping(any(ProductInventoryMapping.class))).thenReturn(saved);

        mockMvc.perform(post("/api/mappings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSaveClone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mappingId", is(10)));

        verify(mappingService).saveMapping(any(ProductInventoryMapping.class));
    }

    @Test
    void update_existing_returnsUpdated() throws Exception {
        ProductInventoryMapping updated = sampleMapping();
        updated.setQuantityRequired(20);
        when(mappingService.updateMapping(eq(10), any(ProductInventoryMapping.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/mappings/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityRequired", is(20)));

        verify(mappingService).updateMapping(eq(10), any(ProductInventoryMapping.class));
    }

    @Test
    void update_notFound_returns404() throws Exception {
        ProductInventoryMapping toUpdate = sampleMapping();
        when(mappingService.updateMapping(eq(10), any(ProductInventoryMapping.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/mappings/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toUpdate)))
                .andExpect(status().isNotFound());

        verify(mappingService).updateMapping(eq(10), any(ProductInventoryMapping.class));
    }

    @Test
    void delete_invokesService() throws Exception {
        doNothing().when(mappingService).deleteMapping(10L);

        mockMvc.perform(delete("/api/mappings/10"))
                .andExpect(status().isNoContent());

        verify(mappingService).deleteMapping(10L);
    }
}
