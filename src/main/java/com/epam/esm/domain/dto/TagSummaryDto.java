package com.epam.esm.domain.dto;

import com.epam.esm.domain.Tag;
import lombok.Data;

import java.time.Instant;

@Data
public class TagSummaryDto {
    private final Tag tag;
    private final double highestCost;

    public TagSummaryDto(long tagId, String tagName, Instant tagCreateDate, Instant tagLastUpdateDate, double highestCost) {
        this.tag = Tag.builder()
                .id(tagId)
                .name(tagName)
                .createDate(tagCreateDate)
                .lastUpdateDate(tagLastUpdateDate)
                .build();
        this.highestCost = highestCost;
    }
}