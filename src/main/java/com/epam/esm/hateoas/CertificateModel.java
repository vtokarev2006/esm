package com.epam.esm.hateoas;

import com.epam.esm.domain.Tag;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Data
public class CertificateModel extends RepresentationModel<CertificateModel> {

    private long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private Instant createDate;
    private Instant lastUpdateDate;
    private List<Tag> tags;
}
