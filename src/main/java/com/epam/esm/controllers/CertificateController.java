package com.epam.esm.controllers;

import com.epam.esm.domain.Certificate;
import com.epam.esm.hateoas.CertificateModel;
import com.epam.esm.hateoas.CertificateModelAssembler;
import com.epam.esm.services.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v2/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;
    private final CertificateModelAssembler certificateModelAssembler;
    private final PagedResourcesAssembler<Certificate> pagedResourcesAssembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<CertificateModel> fetchCertificatesBySearchParamsPageable(@RequestParam Optional<String> name,
                                                                                @RequestParam Optional<String> description,
                                                                                @RequestParam Optional<Set<String>> tagNames,
                                                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {

        Page<Certificate> certificatePage = certificateService.findCertificatesByParams(name, description, tagNames.orElse(Collections.emptySet()), pageable);
        return pagedResourcesAssembler.toModel(certificatePage, certificateModelAssembler);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateModel fetchById(@PathVariable long id) {
        return modelFromCertificate(certificateService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateModel updateById(@PathVariable long id, @RequestBody Certificate certificate) {
        return modelFromCertificate(certificateService.updateById(id, certificate));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        certificateService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateModel create(@RequestBody Certificate certificate) {
        return modelFromCertificate(certificateService.create(certificate));
    }

    private CertificateModel modelFromCertificate(Certificate certificate) {
        CertificateModel certificateModel = certificateModelAssembler.toModel(certificate);
        Link selfLink = linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel();
        certificateModel.add(selfLink);
        return certificateModel;
    }
}