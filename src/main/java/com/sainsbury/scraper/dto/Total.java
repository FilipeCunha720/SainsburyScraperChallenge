package com.sainsbury.scraper.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Total {
    BigDecimal gross;
    BigDecimal vat;

}
