package com.epam.esm.domain.dto;

import com.epam.esm.domain.Tag;
import lombok.Data;

@Data
public class TagOrdersPriceDto {
    private double price;
    private Tag tag;

    public TagOrdersPriceDto(long tagId, String tagName, double price) {
        this.price = price;
        this.tag = Tag.builder()
                .id(tagId)
                .name(tagName)
                .build();
    }
}
