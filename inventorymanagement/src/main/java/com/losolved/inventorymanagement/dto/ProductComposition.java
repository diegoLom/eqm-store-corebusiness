package com.losolved.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductComposition (String productId, String productName, List<ProductComponentDetail> components, BigDecimal calculatedPrice) {

}