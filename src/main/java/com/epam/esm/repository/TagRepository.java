package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagOrdersPriceDto;

import java.util.List;

public interface TagRepository extends GenericRepository<Tag> {
    Tag findByName(String name);
    List<TagOrdersPriceDto> getTagSumOrdersPrice(long userId);

}
