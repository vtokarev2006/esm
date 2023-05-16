package com.epam.esm.controllers;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.hateoas.CertificateModel;
import com.epam.esm.hateoas.CertificateModelAssembler;
import com.epam.esm.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v2/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    private final CertificateModelAssembler certificateModelAssembler;

    private final PagedResourcesAssembler<Certificate> pagedResourcesAssembler;

    @Autowired
    public CertificateController(CertificateService certificateService, CertificateModelAssembler certificateModelAssembler, PagedResourcesAssembler<Certificate> pagedResourcesAssembler) {
        this.certificateService = certificateService;
        this.certificateModelAssembler = certificateModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<CertificateModel>> fetchCertificatesBySearchParamsPageable(@RequestParam Optional<String> name,
                                                                                        @RequestParam Optional<String> description,
                                                                                        @RequestParam Optional<Set<String>> tagNames,
                                                                                        @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {

        Page<Certificate> certificatePage = certificateService.fetchCertificatesBySearchParams(name, description, tagNames.orElse(Collections.emptySet()), pageable);
        PagedModel<CertificateModel> certificateModel = pagedResourcesAssembler.toModel(certificatePage, certificateModelAssembler);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateModel> fetchById(@PathVariable long id) {
        try {
            Certificate certificate = certificateService.fetchById(id);
            CertificateModel certificateModel = modelFromCertificate(certificate);
            return new ResponseEntity<>(certificateModel, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Certificate not found, certificateId = " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateModel> updateById(@PathVariable long id, @RequestBody Certificate certificate) {
        Certificate certificateUpdated = certificateService.updateById(id, certificate);
        CertificateModel certificateModel = modelFromCertificate(certificateUpdated);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        certificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<CertificateModel> create(@RequestBody Certificate certificate) {
        Certificate newCert = certificateService.create(certificate);

        CertificateModel certificateModel = modelFromCertificate(newCert);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCert.getId())
                .toUri();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, String.valueOf(location))
                .body(certificateModel);
    }

    private CertificateModel modelFromCertificate(Certificate certificate) {
        CertificateModel certificateModel = certificateModelAssembler.toModel(certificate);
        Link selfLink = linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel();
        certificateModel.add(selfLink);
        return certificateModel;
    }
}