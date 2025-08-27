package com.ecommerce.inventory.security;

import com.ecommerce.inventory.services.StockLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("stockLevelSecurity")
@RequiredArgsConstructor
public class StockLevelSecurity {

    private final StockLevelService stockLevelService;

    public boolean isOwner(Authentication authentication, Long stockLevelId) {
        Long supplierId = stockLevelService.getSupplierIdByStockLevel(stockLevelId);
        Long authUserId = (Long) authentication.getDetails();
        return supplierId.equals(authUserId);
    }
}
