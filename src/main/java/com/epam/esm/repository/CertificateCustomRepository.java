package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface CertificateCustomRepository {
    PageImpl<Certificate> findCertificatesByNameDescriptionTagNames(
            Optional<String> name,
            Optional<String> description,
            Set<String> tagNames,
            Pageable pageable);
}
