package com.ecommerce.wallet.dtos;


import com.ecommerce.wallet.entities.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private BigDecimal amount;
    private TransactionType type;
}

