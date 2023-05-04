package com.epam.esm.hateoas;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TagModel extends RepresentationModel<TagModel> {
    private long id;
    private String name;
}
