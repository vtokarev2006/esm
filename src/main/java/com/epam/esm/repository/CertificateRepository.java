package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CertificateRepository extends GenericRepository<Certificate> {
    List<Certificate> findByTagName(String tagName);

    List<Certificate> findByNameContainingString(String text);

    List<Certificate> sortByName(boolean asc);

    List<Certificate> sortByDate(boolean asc);

    List<Certificate> getAll(Optional<String> tagName,
                             Optional<String> name,
                             Optional<String> description,
                             Optional<String> orderBy,
                             String orderDirection);

    Certificate patchFields(long id, Map<String, String> fields);

}

