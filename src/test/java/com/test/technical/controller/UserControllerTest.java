package com.test.technical.controller;

import com.test.technical.controller.assembler.UserModelAssembler;
import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.model.User;
import com.test.technical.service.UserService;
import com.test.technical.user.UserTestParent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
public class UserControllerTest extends UserTestParent {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService service;

    @Test
    public void getUserDetailShouldReturnUser() throws Exception {
        User kimiko = getTestUserKimiko();
        mockRepresentationGetUser(kimiko);
        performGetAndExpectValidResponse(kimiko);
    }

    private void performGetAndExpectValidResponse(User user) throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(get("/user/{username}", user.getUsername()))
                .andExpect(status().isOk());

        expectUserInformation(user, resultActions);
    }

    @Test
    public void registerNewUser() throws Exception {
        UserCreationBean creationBean = getValidUserCreationBean();
        User jean = getTestUserJean();
        mockRepresentationRegisterUser(jean);
        performRegisterAndExpectValidResponse(creationBean, jean);
    }

    private void performRegisterAndExpectValidResponse(UserCreationBean creationBean,
                                                       User user) throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(creationBean))
                ).andExpect(status().isCreated());
        expectUserInformation(user, resultActions);
    }

    private void expectUserInformation(User user, ResultActions resultActions) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        resultActions.andExpect(jsonPath("$.username").value(user.getUsername()))
                     .andExpect(jsonPath("$.birthDate").value(sdf.format(user.getBirthDate())))
                     .andExpect(jsonPath("$.countryOfResidence").value(user.getCountryOfResidence()))
                     .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                     .andExpect(jsonPath("$.gender").value(user.getGender().toString()));
    }

    private void mockRepresentationRegisterUser(User user) {
        ResponseEntity<UserRepresentationModel> userRepresentation
                = new ResponseEntity<>(UserModelAssembler.createResourceAndSetSelfLink(user), HttpStatus.CREATED);

        when(service.registerUserAndGetAsRepresentationModel(any()))
                .thenReturn(userRepresentation);
    }

    private void mockRepresentationGetUser(User user) {
        ResponseEntity<UserRepresentationModel> userRepresentation
                = new ResponseEntity<>(UserModelAssembler.createResourceAndSetSelfLink(user), HttpStatus.OK);

        when(service.getUserRepresentationModelByUsername(anyString()))
                .thenReturn(userRepresentation);
    }
}
