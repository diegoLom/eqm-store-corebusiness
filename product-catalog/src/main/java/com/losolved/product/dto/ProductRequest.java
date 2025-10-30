package com.losolved.product.dto;

import java.math.BigDecimal;

public record ProductRequest (String name, String description, BigDecimal basePrice, String category, boolean customizable) {

}
