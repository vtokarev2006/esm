package com.epam.esm.hateoas;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.User;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
public class OrderModel extends RepresentationModel<OrderModel> {
    private long id;
    private String description;
    private double price;
    private Instant createDate;
    private User user;
    private Certificate certificate;
}
