package com.ecommerce.inventory.dtos;


import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.entities.StockLevel;
import com.ecommerce.inventory.entities.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WarehouseDTO {
    private Long warehouseId;

    private String name;

    private String address;

    public static WarehouseDTO fromEntity(Warehouse warehouse) {
        if (warehouse == null) return null;
        return new WarehouseDTO(
                warehouse.getWarehouseId(),
                warehouse.getName(),
                warehouse.getAddress()
        );
    }

    public Warehouse toEntity() {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(warehouseId);
        warehouse.setName(name);
        warehouse.setAddress(address);
        return warehouse;
    }

}
