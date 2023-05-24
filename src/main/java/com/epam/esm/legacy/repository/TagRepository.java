package com.epam.esm.legacy.repository;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagOrdersPriceDto;

import java.util.List;

@Deprecated
public interface TagRepository extends GenericRepository<Tag> {
    Tag fetchByName(String name);
    List<TagOrdersPriceDto> fetchTagSummaryByUserId(long userId);
}
