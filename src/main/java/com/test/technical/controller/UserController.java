package com.test.technical.controller;

import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.logging.annotation.LogIOAndExecutionTime;
import com.test.technical.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired private UserService userService;

    @LogIOAndExecutionTime
    @GetMapping(value="/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<UserRepresentationModel> getUserDetail(@PathVariable final String username) {
        return userService.getUserRepresentationModelByUsername(username);
    }

    @LogIOAndExecutionTime
    @PostMapping
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<UserRepresentationModel> registerUser(@RequestBody @Valid UserCreationBean creationBean) {
        return userService.registerUserAndGetAsRepresentationModel(creationBean);
    }
}
