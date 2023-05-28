package com.epam.esm.hateoas;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagModel extends RepresentationModel<TagModel> {
    private long id;
    private String name;
    private Instant createDate;
    private Instant lastUpdateDate;
}
