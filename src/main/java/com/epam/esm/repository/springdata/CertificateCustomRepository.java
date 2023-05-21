package com.epam.esm.repository.springdata;

import com.epam.esm.domain.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface CertificateCustomRepository {
    Page<Certificate> findCertificatesByNameDescriptionTagNames(
            Optional<String> name,
            Optional<String> description,
            Set<String> tagNames,
            Pageable pageable);
    void refresh(Certificate certificate);
}
