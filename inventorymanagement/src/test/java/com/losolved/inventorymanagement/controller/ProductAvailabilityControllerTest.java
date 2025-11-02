 package com.losolved.inventorymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losolved.inventorymanagement.dto.ProductAvailability;
import com.losolved.inventorymanagement.services.ProductAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.instancio.Instancio;
import static org.instancio.Select.field;

@ExtendWith(MockitoExtension.class)
public class ProductAvailabilityControllerTest {

   private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private ProductAvailabilityService availabilityService;

    @InjectMocks
    private ProductAvailabilityController availabilityController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(availabilityController).build();
    }


    @Test
    void singleMethod_getAvailabilityByProduct_returnsOkOrNotFound() throws Exception {

       ProductAvailability random = Instancio.of(ProductAvailability.class)
                .set(field(ProductAvailability.class, "isAvailable"), true)
                .create();

        when(availabilityService.getProductAvailability(100)).thenReturn(random);

        mockMvc.perform(get("/api/availability/product/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(random.productId())))
                .andExpect(jsonPath("$.quantityAvailable", is(random.quantityAvailable())))
                .andExpect(jsonPath("$.isAvailable", is(random.isAvailable()))

         );

        verify(availabilityService).getProductAvailability(100);

        ProductAvailability specific = Instancio.of(ProductAvailability.class)
                .set(field(ProductAvailability.class, "productId"), 200)
                .set(field(ProductAvailability.class, "isAvailable"), false)
                .set(field(ProductAvailability.class, "quantityAvailable"), 0)
                .create();

        when(availabilityService.getProductAvailability(200)).thenReturn(specific);

        mockMvc.perform(get("/api/availability/product/200"))
                .andExpect(status().isNotFound());

        verify(availabilityService).getProductAvailability(200);
    }
}
