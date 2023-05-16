package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Deprecated
public interface CertificateRepository extends GenericRepository<Certificate> {
    List<Certificate> fetchCertificatesBySearchParams(Optional<String> tagName,
                                                      Optional<String> name,
                                                      Optional<String> description,
                                                      Optional<String> orderBy,
                                                      String orderDirection);
    Certificate partialUpdate(long id, Map<String, String> fields);
}

