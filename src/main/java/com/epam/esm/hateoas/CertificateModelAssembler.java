package com.epam.esm.hateoas;

import com.epam.esm.controllers.v2.CertificateController;
import com.epam.esm.domain.Certificate;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CertificateModelAssembler extends RepresentationModelAssemblerSupport<Certificate, CertificateModel> {

    public CertificateModelAssembler() {
        super(CertificateController.class, CertificateModel.class);
    }

    @Override
    public CertificateModel toModel(Certificate certificate) {
        CertificateModel certificateModel = new CertificateModel();
        BeanUtils.copyProperties(certificate, certificateModel);
        return certificateModel;
    }
}
