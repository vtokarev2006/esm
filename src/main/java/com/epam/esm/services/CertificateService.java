package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    public Page<Certificate> findCertificatesByParams(Optional<String> name, Optional<String> description, Set<String> tagNames, Pageable pageable) {
        return certificateRepository.findCertificatesByNameDescriptionTagNames(name, description, tagNames, pageable);
    }

    public Certificate findById(long id) {
        return certificateRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Certificate not found, certificateId = " + id, ErrorCode.CertificateNotExist));
    }

    @Transactional
    public Certificate updateById(long certificateId, Certificate certificate) {
        Certificate certificateToSave = certificateRepository
                .findForUpdateById(certificateId)
                .orElseThrow(() -> new ResourceDoesNotExistException("Certificate doesn't exist, id = " + certificateId, ErrorCode.CertificateNotExist));

        Set<String> excludedFieldsFromUpdate = getNullPropertyNames(certificate);
        excludedFieldsFromUpdate.addAll(Set.of("id", "createDate", "lastUpdateDate", "tags"));
        BeanUtils.copyProperties(certificate, certificateToSave, excludedFieldsFromUpdate.toArray(new String[0]));
        if (certificate.getTags() != null) {
            saveTags(certificate, certificateToSave);
        }
        return certificateToSave;
    }

    public void delete(long id) {
        if (!certificateRepository.existsById(id))
            throw new ResourceDoesNotExistException("Certificate not found, certificateId=" + id, ErrorCode.CertificateNotExist);
        certificateRepository.deleteById(id);
    }

    @Transactional
    public Certificate create(Certificate certificate) {
        certificate.setId(null);
        if(certificate.getTags() == null) {
            certificate.setTags(Collections.emptySet());
        } else {
            saveTags(certificate, certificate);
        }
        return certificateRepository.save(certificate);
    }

    private void saveTags(Certificate certificate, Certificate certificateToSave) {
        Set<Tag> tagToSave = new HashSet<>();
        for (Tag tag : certificate.getTags()) {
            if (tag.getId() == null) {
                try {
                    tagToSave.add(tagRepository.save(tag));
                } catch (DataIntegrityViolationException e) {
                    throw new TagDuplicateNameException("Tag already exists, name = " + tag.getName());
                }
            } else {
                Tag newTag = tagRepository.findById(tag.getId()).orElseThrow(() -> new ResourceDoesNotExistException("Tag doesnt exist, tagId = " + tag.getId(), ErrorCode.TagNotExist));
                try {
                    Set<String> excludedFieldsFromUpdate = getNullPropertyNames(tag);
                    excludedFieldsFromUpdate.addAll(Set.of("id", "createDate", "lastUpdateDate"));
                    BeanUtils.copyProperties(tag, newTag, excludedFieldsFromUpdate.toArray(new String[0]));
                    tagToSave.add(newTag);
                } catch (DataIntegrityViolationException e) {
                    throw new TagDuplicateNameException("Tag already exists, name = " + newTag.getName());
                }
            }
        }
        certificateToSave.setTags(tagToSave);
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