package com.epam.esm.hateoas;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserModel extends RepresentationModel<UserModel> {
    private long id;
    private String email;
    private Instant createDate;
    private Instant lastUpdateDate;
}

