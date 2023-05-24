package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface CertificateCustomRepository {
    Page<Certificate> findCertificatesByNameDescriptionTagNames(
            Optional<String> name,
            Optional<String> description,
            Set<String> tagNames,
            Pageable pageable);
    @Transactional
    void refresh(Certificate certificate);
}
