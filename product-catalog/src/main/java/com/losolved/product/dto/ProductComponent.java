package com.losolved.product.dto;

public record ProductComponent(String componentType, String componentName, Boolean required, Integer minSelection, Integer maxSelection) {
}
