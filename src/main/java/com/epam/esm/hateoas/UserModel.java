package com.epam.esm.hateoas;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UserModel extends RepresentationModel<UserModel> {
    private long id;
    private String email;
}

