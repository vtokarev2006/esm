package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class Certificate {
    private long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private Instant createDate;
    private Instant lastUpdateDate;
    private List<Tag> tags;
}
