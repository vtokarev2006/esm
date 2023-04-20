package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<Certificate> getAll(Optional<String> tagName,
                                    Optional<String> name,
                                    Optional<String> description,
                                    Optional<String> orderBy,
                                    String orderDirection) {
        return certificateRepository.getAll(tagName, name, description, orderBy, orderDirection);
    }

    public Certificate get(long id) {
        return certificateRepository.get(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public boolean update(Certificate certificate) {
        return certificateRepository.update(certificate);
    }

    public boolean delete(long id) {
        return certificateRepository.delete(id);
    }

    public Certificate create(Certificate certificate) {
        return certificateRepository.create(certificate);
    }
}
