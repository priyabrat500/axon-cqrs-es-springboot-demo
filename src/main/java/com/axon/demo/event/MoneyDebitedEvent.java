package com.axon.demo.event;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class MoneyDebitedEvent {

    private final UUID id;
    private final BigDecimal debitAmount;
}
