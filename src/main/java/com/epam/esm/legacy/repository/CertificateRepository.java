package com.epam.esm.legacy.repository;

import com.epam.esm.domain.Certificate;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface CertificateRepository extends GenericRepository<Certificate> {
    List<Certificate> fetchCertificatesBySearchParams(Optional<String> tagName,
                                                      Optional<String> name,
                                                      Optional<String> description,
                                                      Optional<String> orderBy,
                                                      String orderDirection);
    Certificate updateById(long id, Certificate certificate);
}

