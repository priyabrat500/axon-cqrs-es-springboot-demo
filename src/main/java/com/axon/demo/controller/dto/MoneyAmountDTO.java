package com.axon.demo.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MoneyAmountDTO {
    private BigDecimal amount;
}
