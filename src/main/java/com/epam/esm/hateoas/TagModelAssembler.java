package com.epam.esm.hateoas;

import com.epam.esm.controllers.v2.TagController;
import com.epam.esm.domain.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<Tag, TagModel> {

    public TagModelAssembler() {
        super(TagController.class, TagModel.class);
    }

    @Override
    public TagModel toModel(Tag entity) {
        TagModel tagModel = new TagModel();
        BeanUtils.copyProperties(entity, tagModel);
        return tagModel;
    }
}