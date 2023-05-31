package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class CertificateCustomRepositoryImplTests {

    @Autowired
    CertificateRepository certificateRepository;

    @ParameterizedTest
    @MethodSource("testData")
    void findCertificatesByNameDescriptionTagNames_test(Optional<String> name,
                                                        Optional<String> description,
                                                        Set<String> tagNames,
                                                        Pageable pageable,
                                                        int size,
                                                        int index,
                                                        Certificate certificate) {

        List<Certificate> certificatesFromDb = certificateRepository
                .findCertificatesByNameDescriptionTagNames(name, description, tagNames, pageable)
                .getContent();

        assertEquals(size, certificatesFromDb.size());

        Certificate certificateFromDb = certificatesFromDb.get(index);

        assertEquals(certificate.getName(), certificateFromDb.getName());
        assertEquals(certificate.getDescription(), certificateFromDb.getDescription());
        assertEquals(certificate.getDuration(), certificateFromDb.getDuration());
        assertEquals(certificate.getTags(), certificateFromDb.getTags());
    }

    public static Stream<Arguments> testData() {
        return Stream.of(
                arguments(Optional.empty(),
                        Optional.empty(),
                        Collections.emptySet(),
                        PageRequest.of(0, 30, Sort.by("id")),
                        10,
                        0,
                        Certificate.builder()
                                .id(1L)
                                .name("Rick Kleiner")
                                .description("Paige Turner")
                                .price(105.59)
                                .duration(3)
                                .tags(Set.of(Tag.builder()
                                        .id(2L)
                                        .name("Tag002")
                                        .createDate(Instant.parse("2023-05-21T17:40:01Z"))
                                        .lastUpdateDate(Instant.parse("2023-05-21T17:40:40Z"))
                                        .build()))
                                .build()),
                arguments(Optional.of("t"),
                        Optional.of("n"),
                        Set.of("Tag005", "Tag006"),
                        PageRequest.of(0, 30, Sort.by("id")),
                        1,
                        0,
                        Certificate.builder()
                                .id(7L)
                                .name("Oliver Sutton")
                                .description("Evan Lee Arps")
                                .price(48.41)
                                .duration(11)
                                .tags(Set.of(
                                        Tag.builder()
                                                .id(3L)
                                                .name("Tag003")
                                                .createDate(Instant.parse("2023-05-21T17:40:01Z"))
                                                .lastUpdateDate(Instant.parse("2023-05-21T17:40:40Z"))
                                                .build(),
                                        Tag.builder()
                                                .id(5L)
                                                .name("Tag005")
                                                .createDate(Instant.parse("2023-05-21T17:40:01Z"))
                                                .lastUpdateDate(Instant.parse("2023-05-21T17:40:40Z"))
                                                .build(),
                                        Tag.builder()
                                                .id(6L)
                                                .name("Tag006")
                                                .createDate(Instant.parse("2023-05-21T17:40:01Z"))
                                                .lastUpdateDate(Instant.parse("2023-05-21T17:40:40Z"))
                                                .build()
                                ))
                                .build())
        );
    }
}
