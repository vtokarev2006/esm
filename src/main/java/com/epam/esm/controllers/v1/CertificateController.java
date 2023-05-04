package com.epam.esm.controllers.v1;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public List<Certificate> getAll(@RequestParam Optional<String> tagName,
                                    @RequestParam Optional<String> name,
                                    @RequestParam Optional<String> description,
                                    @RequestParam Optional<String> orderBy,
                                    @RequestParam(defaultValue = "ASC") String orderDirection) {
        return certificateService.getAll(tagName, name, description, orderBy, orderDirection);
    }

    @GetMapping("/{id}")
    public Certificate getById(@PathVariable long id) {
        try {
            return certificateService.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Certificate not found, certificateId = " + id);
        }
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Certificate certificate) {
        certificateService.update(certificate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        if (!certificateService.delete(id))
            throw new ResourceDoesNotExistException("Certificate not found, certificateId = " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Certificate certificate) {
        Certificate newCert = certificateService.create(certificate);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCert.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchFields(@PathVariable long id, @RequestBody Map<String, String> fields) {
        if (fields == null || fields.isEmpty() || id <= 0) {
            throw new BadRequestException("Wrong format for patch Certificate, id = " + id);
        }
        certificateService.patchFields(id, fields);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}