package com.losolved.inventorymanagement.dto;

import java.math.BigDecimal;

public record ProductComponentDetail(String componentType, String itemId, String itemName, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice){

}

