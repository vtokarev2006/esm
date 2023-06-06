package com.epam.esm.hateoas;

import com.epam.esm.domain.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserModel extends RepresentationModel<UserModel> {
    private long id;
    private String email;
    private Role role;
    private Instant createDate;
    private Instant lastUpdateDate;
}

