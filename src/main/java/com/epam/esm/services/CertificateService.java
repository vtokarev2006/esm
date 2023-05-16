package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.repository.springdata.CertificateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CertificateService {
    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public Page<Certificate> fetchCertificatesBySearchParams(Optional<String> name, Optional<String> description, Set<String> tagNames, Pageable pageable) {
        return certificateRepository.findCertificatesByNameDescriptionTagNames(name, description, tagNames, pageable);
    }

    public Certificate fetchById(long id) {
        return certificateRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Transactional
    public Certificate updateById(long certificateId, Certificate certificate) {
        Certificate certificateToSave = certificateRepository
                .findForUpdateById(certificateId)
                .orElseThrow(() -> new ResourceDoesNotExistException("Certificate doesn't exist, id = " + certificateId));

        log.trace("certificate:" + certificate);
        log.trace("certificateToSave:" + certificateToSave);

        Set<String> excludedFieldsFromUpdate = getNullPropertyNames(certificate);
        excludedFieldsFromUpdate.addAll(Set.of("id", "createDate", "lastUpdateDate"));

        log.trace("excludedFieldsFromUpdate:" + excludedFieldsFromUpdate);

        BeanUtils.copyProperties(certificate, certificateToSave, excludedFieldsFromUpdate.toArray(new String[0]));
        certificateToSave.setLastUpdateDate(Instant.now());

        log.trace("certificateToSave after copyProperties:" + certificateToSave);

        try {
            return certificateRepository.save(certificateToSave);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new TagDuplicateNameException("A duplicate tag name was passed for tag creation during the certificate update query");
        }
    }

    public void delete(long id) {
        certificateRepository.deleteById(id);
    }

    public Certificate create(Certificate certificate) {
        Instant now = Instant.now();
        Certificate certificateToCreate = Certificate
                .builder()
                .createDate(now)
                .lastUpdateDate(now)
                .build();
        BeanUtils.copyProperties(certificate, certificateToCreate, "id", "createDate", "lastUpdateDate");
        return certificateRepository.save(certificateToCreate);
    }

    private Set<String> getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames;
    }
}