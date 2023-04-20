package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tag {
    private long id;
    private String name;
}
