package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class CertificateServiceTests {
    private final Faker faker = new Faker();
    private final Random r = new Random();

    @Test
    void findCertificatesByParams_test() {

        Optional<String> name = Optional.of(faker.name().name());
        Optional<String> description = Optional.of(faker.name().name());
        Set<String> tagNames = Set.of(faker.funnyName().name() + "1", faker.funnyName().name() + "2");
        Pageable pageable = PageRequest.of(r.nextInt(10), r.nextInt(1, 10));
        PageImpl<Certificate> certificatePage = new PageImpl<>(new ArrayList<>(List.of(Certificate.builder().id(1L).build(),
                Certificate.builder().id(2L).build(),
                Certificate.builder().id(3L).build()
        )), pageable, r.nextInt(500, 1000));

        TagRepository tagRepository = mock(TagRepository.class);
        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.findCertificatesByNameDescriptionTagNames(name, description, tagNames, pageable)).thenReturn(certificatePage);
        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);

        assertEquals(certificatePage, certificateService.findCertificatesByParams(name, description, tagNames, pageable));
        verify(certificateRepository).findCertificatesByNameDescriptionTagNames(name, description, tagNames, pageable);
    }

    @Test
    void findById_certificate_not_exist_test() {
        long certificateId = r.nextLong(1, Long.MAX_VALUE);
        TagRepository tagRepository = mock(TagRepository.class);
        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());
        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);
        ResourceDoesNotExistException thrown = assertThrows(ResourceDoesNotExistException.class, () -> certificateService.findById(certificateId));
        assertSame(thrown.getErrorCode(), ErrorCode.CertificateNotExist);
    }

    @Test
    void findById_test() {
        long certificateId = r.nextLong(1, Long.MAX_VALUE);
        Certificate certificate = Certificate.builder()
                .id(r.nextLong(1, Long.MAX_VALUE))
                .name(faker.funnyName().name())
                .description(faker.funnyName().name())
                .tags(Set.of(Tag.builder().id(r.nextLong(1, Long.MAX_VALUE)).build(),
                        Tag.builder().id(r.nextLong(1, Long.MAX_VALUE)).build()))
                .build();
        TagRepository tagRepository = mock(TagRepository.class);
        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));
        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);
        assertEquals(certificate, certificateService.findById(certificateId));
    }

    @Test
    void updateById_certificate_not_exist_test() {
        long certificateId = r.nextLong(1, Long.MAX_VALUE);

        TagRepository tagRepository = mock(TagRepository.class);

        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.findForUpdateById(certificateId)).thenReturn(Optional.empty());

        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);

        ResourceDoesNotExistException thrown = assertThrows(ResourceDoesNotExistException.class,
                () -> certificateService.updateById(certificateId, Certificate.builder().id(r.nextLong(1, Long.MAX_VALUE)).build()));
        assertSame(ErrorCode.CertificateNotExist, thrown.getErrorCode());
    }

    @Test
    void updateById_tag_not_exist_test() {
        long tagId = r.nextLong(1, Long.MAX_VALUE);
        long certificateId = r.nextLong(1, Long.MAX_VALUE);
        Tag tag = Tag.builder().id(tagId).build();
        Certificate certificate = Certificate.builder()
                .id(r.nextLong(1, Long.MAX_VALUE))
                .tags(Set.of(tag))
                .build();

        TagRepository tagRepository = mock(TagRepository.class);
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.empty());

        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.findForUpdateById(certificateId)).thenReturn(Optional.of(Certificate.builder().id(r.nextLong(1, Long.MAX_VALUE)).build()));

        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);

        ResourceDoesNotExistException thrown =
                assertThrows(ResourceDoesNotExistException.class, () -> certificateService.updateById(certificateId, certificate));
        assertSame(ErrorCode.TagNotExist, thrown.getErrorCode());
    }

    @Test
    void updateById_test(){
        long certificateId = r.nextLong(1, Long.MAX_VALUE);
        long tagId1 = r.nextLong(1, Long.MAX_VALUE);
        long tagId2 = r.nextLong(1, Long.MAX_VALUE);

        Tag tag1 = Tag.builder().id(tagId1).name(faker.funnyName().name()).build();
        Tag tag2 = Tag.builder().name(faker.funnyName().name()).build();

        TagRepository tagRepository = mock(TagRepository.class);
        when(tagRepository.findById(tag1.getId())).thenReturn(Optional.of(tag1));

        Tag savedTag2 = Tag.builder().id(tagId2).name(tag2.getName()).build();
        when(tagRepository.save(tag2)).thenReturn(savedTag2);

        Certificate certificate = Certificate.builder()
                .name(faker.funnyName().name())
                .description(faker.funnyName().name())
                .tags(Set.of(tag1, tag2))
                .price(r.nextDouble(1, Double.MAX_VALUE))
                .duration(r.nextInt(1, Integer.MAX_VALUE))
                .build();

        Certificate certificateToUpdate = Certificate.builder()
                .id(certificateId)
                .build();

        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.findForUpdateById(certificateId)).thenReturn(Optional.of(certificateToUpdate));

        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);

        Certificate certificateAfterUpdate = certificateService.updateById(certificateId, certificate);
        certificate.setId(certificateId);
        certificate.setTags(Set.of(tag1,savedTag2));
        assertEquals(certificate, certificateAfterUpdate);
    }

    @Test
    void delete_certificate_not_exist_test() {
        long certificateId = r.nextLong(1, Long.MAX_VALUE);
        TagRepository tagRepository = mock(TagRepository.class);
        CertificateRepository certificateRepository = mock(CertificateRepository.class);
        when(certificateRepository.existsById(certificateId)).thenReturn(false);
        CertificateService certificateService = new CertificateService(certificateRepository, tagRepository);
        ResourceDoesNotExistException thrown = assertThrows(ResourceDoesNotExistException.class, () -> certificateService.delete(certificateId));
        assertSame(thrown.getErrorCode(), ErrorCode.CertificateNotExist);
    }
}