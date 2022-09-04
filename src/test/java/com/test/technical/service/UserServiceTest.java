package com.test.technical.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.technical.controller.errorhandling.exception.InvalidUserException;
import com.test.technical.controller.errorhandling.exception.UserAlreadyExistsException;
import com.test.technical.controller.errorhandling.exception.UserNotFoundException;
import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.dto.UserRepresentationBean;
import com.test.technical.user.UserTestParent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest extends UserTestParent {

    @Autowired
    private UserService userService;

    @Autowired
    public UserServiceTest(ObjectMapper objectMapper) {
        super(objectMapper);
    }


    @Test
    public void givenUsername_whenQueried_thenReturnUserRepresentation() {
        ResponseEntity<UserRepresentationModel> userRepresentation =
                userService.getUserRepresentationModelByUsername("Kimiko");

        assertEquals(HttpStatus.OK, userRepresentation.getStatusCode());
        if(userRepresentation.getBody() != null) {
            UserRepresentationBean bean = UserRepresentationBean.fromModel(getTestUserKimiko());
            assertEquals(bean, userRepresentation.getBody().getUser());
        }
    }

    @Test
    public void givenUnknownUser_whenQueried_thenThrowsException(){
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.getUserRepresentationModelByUsername("Unknown")
        );

        assertTrue(exception.getMessage().contains("Cannot find an user with the specified username : Unknown"));
    }

    @Test
    public void givenValidRegistrationBean_whenRegistering_thenReturnCreatedUserRepresentation() {
        UserCreationBean creationBean = getValidUserCreationBean();
        ResponseEntity<UserRepresentationModel> createdUserRepresentation =
                userService.registerUserAndGetAsRepresentationModel(creationBean);

        assertEquals(HttpStatus.CREATED, createdUserRepresentation.getStatusCode());
        if(createdUserRepresentation.getBody() != null) {
            UserRepresentationBean bean = UserRepresentationBean.fromModel(getTestUserJean());
            assertEquals(bean, createdUserRepresentation.getBody().getUser());
        }
    }

    @Test
    public void givenInvalidBirthDate_whenRegistering_thenThrows() {
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setBirthDate(LocalDate.parse("birthDate"));
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("Text 'birthDate' could not be parsed at index 0"));
    }

    @Test
    public void givenFutureBirthDate_whenRegistering_thenThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setBirthDate(null);
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("Cannot retrieve the age of the user, missing birth date"));
    }

    @Test
    public void givenBadGender_whenRegistering_thenThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setGender("INVALID_GENDER");
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("No enum constant com.test.technical.model.Gender.INVALID_GENDER"));
    }

    @Test
    public void givenNonLivingInFranceUser_whenRegistering_thenThrows() {
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setCountryOfResidence("Portugal");
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("The given user is either not living in France and/or not an adult"));
    }

    @Test
    public void givenNonNotAdultUser_whenRegistering_thenThrows() {
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setBirthDate(LocalDate.parse("2010-08-05"));
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("The given user is either not living in France and/or not an adult"));
    }

    @Test
    public void givenNonNotAdultUserAndNotLivingInFrance_whenRegistering_thenThrows() {

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setCountryOfResidence("Portugal");
            creationBean.setBirthDate(LocalDate.parse("2010-08-05"));
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("The given user is either not living in France and/or not an adult"));
    }

    @Test
    public void givenAlreadyExistingUser_whenRegistering_thenThrows() {
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            UserCreationBean creationBean = getValidUserCreationBean();
            creationBean.setUsername("Martin");
            userService.registerUserAndGetAsRepresentationModel(creationBean);
        });

        assertTrue(exception.getMessage().contains("An user already exists for this username : Martin"));
    }
}
