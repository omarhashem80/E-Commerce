package com.ecommerce.shop.dtos;

import com.ecommerce.shop.entities.Status;

public class StatusUpdateRequest {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

