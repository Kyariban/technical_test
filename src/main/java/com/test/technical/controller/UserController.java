package com.test.technical.controller;

import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.logging.annotation.LogIOAndExecutionTime;
import com.test.technical.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @LogIOAndExecutionTime
    @GetMapping(value="/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRepresentationModel> getUserDetail(@PathVariable final String username) {
        return userService.getUserRepresentationModelByUsername(username);
    }

    @LogIOAndExecutionTime
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRepresentationModel> registerUser(@RequestBody @Valid UserCreationBean creationBean) {
        return userService.registerUserAndGetAsRepresentationModel(creationBean);
    }
}
