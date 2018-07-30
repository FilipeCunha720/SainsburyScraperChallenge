package com.sainsbury.scraper.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    String title;
    BigDecimal kcal_per_100g;
    BigDecimal unit_price;
    String description;

}
