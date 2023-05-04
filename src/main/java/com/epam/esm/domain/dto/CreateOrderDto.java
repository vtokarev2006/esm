package com.epam.esm.domain.dto;

import lombok.Data;

@Data
public class CreateOrderDto {
    private String description;
    private long certificateId;
    private long userId;
}
