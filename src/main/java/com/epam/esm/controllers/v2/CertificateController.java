package com.epam.esm.controllers.v2;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.BadRequestException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController("CertificateControllerV2")
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
    public ResponseEntity<PagedModel<CertificateModel>> getAll(@RequestParam Optional<String> name,
                                    @RequestParam Optional<String> description,
                                    @RequestParam Optional<Set<String>> tagNames,
                                    @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {

        Page<Certificate> certificatePage = certificateService.getAll(name, description, tagNames.orElse(Collections.emptySet()), pageable);
        PagedModel<CertificateModel> certificateModel = pagedResourcesAssembler.toModel(certificatePage, certificateModelAssembler);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateModel> getById(@PathVariable long id) {
        try {
            Certificate certificate = certificateService.get(id);
            CertificateModel certificateModel = modelFromCertificate(certificate);
            return new ResponseEntity<>(certificateModel, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Certificate not found, certificateId = " + id);
        }
    }

    @PutMapping
    public ResponseEntity<CertificateModel> update(@RequestBody Certificate certificate) {
        certificateService.update(certificate);
        CertificateModel certificateModel = modelFromCertificate(certificate);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        if (!certificateService.delete(id))
            throw new ResourceDoesNotExistException("Certificate not found, certificateId = " + id);
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

    /**
     * Updating one or more Certificate fields by providing in the body of patch request Map Entries: key = field name, value = new field's value.
     *
     * @param id     - Certificated ID
     * @param fields - Map<fieldName, fieldNewValue>
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CertificateModel> patchFields(@PathVariable long id, @RequestBody Map<String, String> fields) {
        if (fields == null || fields.isEmpty() || id <= 0) {
            throw new BadRequestException("Wrong format for patch Certificate, id = " + id);
        }
        Certificate certificate = certificateService.patchFields(id, fields);
        CertificateModel certificateModel = modelFromCertificate(certificate);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    private CertificateModel modelFromCertificate(Certificate certificate) {
        CertificateModel certificateModel = certificateModelAssembler.toModel(certificate);
        Link selfLink = linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel();
        certificateModel.add(selfLink);
        return certificateModel;
    }
}