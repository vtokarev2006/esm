package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagSummaryDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class TagRepositoryTests {
    @Autowired
    TagRepository tagRepository;

    @ParameterizedTest
    @MethodSource("testData")
    void findTagSummaryByUserId(long userId, long tagId, double highestCost) {
        Tag tag = tagRepository.findById(tagId).orElseThrow();
        TagSummaryDto tagSummaryDto = tagRepository.findTagSummaryByUserId(userId).orElseThrow();
        assertEquals(tag.getId(), tagSummaryDto.getTag().getId());
        assertEquals(highestCost, tagSummaryDto.getHighestCost());
    }

    public static Stream<Arguments> testData() {
        return Stream.of(
                arguments(1, 2, 42.99),
                arguments(5, 4, 76.02),
                arguments(9, 1, 101.31)
        );
    }
}
