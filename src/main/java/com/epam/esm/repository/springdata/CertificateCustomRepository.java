package com.epam.esm.repository.springdata;

import com.epam.esm.domain.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CertificateCustomRepository {
    Page<Certificate> findCertificateByNameDescriptionTagNames(
            Optional<String> name,
            Optional<String> description,
            Set<String> tagNames,
            Pageable pageable);

}
