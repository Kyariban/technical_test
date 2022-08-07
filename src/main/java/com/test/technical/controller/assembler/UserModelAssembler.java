package com.test.technical.controller.assembler;

import com.test.technical.controller.UserController;
import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.model.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserRepresentationModel> {

    @Override
    public UserRepresentationModel toModel(User user) {
        return createResourceAndSetSelfLink(user);
    }

    private UserRepresentationModel createResourceAndSetSelfLink(User user) {
        UserRepresentationModel userRepresentationModel = new UserRepresentationModel(user);

        Link selfLink = linkTo(methodOn(UserController.class).getUserDetail(user.getUsername()))
                .withSelfRel();

        userRepresentationModel.add(selfLink);

        return userRepresentationModel;
    }
}
