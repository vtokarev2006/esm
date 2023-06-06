package com.epam.esm.hateoas;

import com.epam.esm.controllers.UserController;
import com.epam.esm.domain.User;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User user) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        return userModel;
    }
}