package com.epam.esm.hateoas;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderModel extends RepresentationModel<OrderModel> {
    private long id;
    private String description;
    private Double price;
    private Instant createDate;
    private Instant lastUpdateDate;
    private User user;
    private Certificate certificate;
}
