package com.epam.esm.hateoas;

import com.epam.esm.controllers.TagController;
import com.epam.esm.domain.dto.TagSummaryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TagSummaryDtoModelAssembler extends RepresentationModelAssemblerSupport<TagSummaryDto, TagSummaryDtoModel> {

    public TagSummaryDtoModelAssembler() {
        super(TagController.class, TagSummaryDtoModel.class);
    }

    @Override
    public TagSummaryDtoModel toModel(TagSummaryDto entity) {
        TagSummaryDtoModel tagSummaryDtoModel = new TagSummaryDtoModel();
        BeanUtils.copyProperties(entity, tagSummaryDtoModel);
        return tagSummaryDtoModel;
    }
}