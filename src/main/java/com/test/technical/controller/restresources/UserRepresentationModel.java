package com.test.technical.controller.restresources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.test.technical.model.User;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@JsonIgnoreProperties({ "id" })
public class UserRepresentationModel extends RepresentationModel<UserRepresentationModel>{

    @JsonUnwrapped
    private final User user;

    public UserRepresentationModel(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRepresentationModel that = (UserRepresentationModel) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}
