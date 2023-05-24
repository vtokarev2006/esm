package com.epam.esm.hateoas;

import com.epam.esm.domain.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagSummaryDtoModel extends RepresentationModel<TagSummaryDtoModel> {
    private Tag tag;
    private double highestCost;
}
