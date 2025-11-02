package com.losolved.inventorymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losolved.inventorymanagement.model.InventoryItem;
import com.losolved.inventorymanagement.services.InventoryItemService;
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
public class InventoryItemControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private InventoryItemService inventoryItemService;

    @InjectMocks
    private InventoryItemController inventoryItemController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryItemController).build();
    }

    private InventoryItem sampleItem() {
        InventoryItem it = new InventoryItem();
        try {
            // set common fields if setters exist
            it.setItemId(1);
            it.setSku("SKU-1");
            it.setName("Sample");
      //      it.setQuantity(10);
        } catch (Exception ignored) { }
        return it;
    }

    @Test
    void getAll_returnsList() throws Exception {
        InventoryItem it = sampleItem();
        when(inventoryItemService.getAllItems()).thenReturn(Collections.singletonList(it));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].itemId", is(1)));

        verify(inventoryItemService, times(1)).getAllItems();
    }

    @Test
    void getById_found() throws Exception {
        InventoryItem it = sampleItem();
        when(inventoryItemService.getItemById(1)).thenReturn(Optional.of(it));

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.sku", is("SKU-1")));

        verify(inventoryItemService).getItemById(1);
    }

    @Test
    void getById_notFound() throws Exception {
        when(inventoryItemService.getItemById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isNotFound());

        verify(inventoryItemService).getItemById(1);
    }

    @Test
    void getBySku_found() throws Exception {
        InventoryItem it = sampleItem();
        when(inventoryItemService.getBySku("SKU-1")).thenReturn(Optional.of(it));

        mockMvc.perform(get("/api/items/sku/SKU-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is("SKU-1")));

        verify(inventoryItemService).getBySku("SKU-1");
    }

    @Test
    void getBySku_notFound() throws Exception {
        when(inventoryItemService.getBySku("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/items/sku/NOPE"))
                .andExpect(status().isNotFound());

        verify(inventoryItemService).getBySku("NOPE");
    }

    @Test
    void searchByName_returnsResults() throws Exception {
        InventoryItem it = sampleItem();
        when(inventoryItemService.searchByName("Sample")).thenReturn(Collections.singletonList(it));

        mockMvc.perform(get("/api/items/search").param("name", "Sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Sample")));

        verify(inventoryItemService).searchByName("Sample");
    }

    @Test
    void create_returnsSaved() throws Exception {
        InventoryItem toSave = sampleItem();
        toSave.setItemId(null); // simulate new
        InventoryItem saved = sampleItem();
        when(inventoryItemService.saveItem(any(InventoryItem.class))).thenReturn(saved);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is(1)));

        verify(inventoryItemService).saveItem(any(InventoryItem.class));
    }

    @Test
    void delete_invokesService() throws Exception {
        doNothing().when(inventoryItemService).deleteItem(1);

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());

        verify(inventoryItemService).deleteItem(1);
    }

    @Test
    void update_nullBody_returnsBadRequest() throws Exception {
        // sending an empty body / invalid JSON results in 400
        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_existing_returnsUpdated() throws Exception {
        InventoryItem existing = sampleItem();
        when(inventoryItemService.getItemById(1)).thenReturn(Optional.of(existing));

        InventoryItem toUpdate = sampleItem();
        toUpdate.setName("Updated");
        when(inventoryItemService.saveItem(any(InventoryItem.class))).thenReturn(toUpdate);

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated")));

        verify(inventoryItemService).getItemById(1);
        verify(inventoryItemService).saveItem(any(InventoryItem.class));
    }

    @Test
    void update_notFound_returns404() throws Exception {
        when(inventoryItemService.getItemById(1)).thenReturn(Optional.empty());

        InventoryItem toUpdate = sampleItem();
        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toUpdate)))
                .andExpect(status().isNotFound());

        verify(inventoryItemService).getItemById(1);
        verify(inventoryItemService, never()).saveItem(any());
    }
}