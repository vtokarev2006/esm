package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final com.epam.esm.repository.springdata.CertificateRepository certificateRepositorySpringData;

    public CertificateService(CertificateRepository certificateRepository, com.epam.esm.repository.springdata.CertificateRepository certificateRepositorySpringData) {
        this.certificateRepository = certificateRepository;
        this.certificateRepositorySpringData = certificateRepositorySpringData;
    }

    public List<Certificate> getAll(Optional<String> tagName,
                                    Optional<String> name,
                                    Optional<String> description,
                                    Optional<String> orderBy,
                                    String orderDirection) {

        return certificateRepository.getAll(tagName, name, description, orderBy, orderDirection);
    }
    public Page<Certificate> getAll(Optional<String> name,
                                    Optional<String> description,
                                    Set<String> tagNames,
                                    Pageable pageable) {
        return certificateRepositorySpringData.findCertificateByNameDescriptionTagNames(name, description, tagNames, pageable);
    }



    public Certificate get(long id) {
        return certificateRepository.get(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public void update(Certificate certificate) {
        certificateRepository.update(certificate);
    }

    public Certificate patchFields(long id, Map<String, String> fields) {
        return certificateRepository.patchFields(id, fields);
    }

    public boolean delete(long id) {
        return certificateRepository.delete(id);
    }

    public Certificate create(Certificate certificate) {
        return certificateRepository.create(certificate);
    }


}
