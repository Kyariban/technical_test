package com.test.technical.service;

import com.test.technical.controller.errorhandling.exception.InvalidUserException;
import com.test.technical.controller.errorhandling.exception.UserAlreadyExistsException;
import com.test.technical.controller.errorhandling.exception.UserNotFoundException;
import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.model.Gender;
import com.test.technical.model.User;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void givenUsername_whenQueried_thenReturnUserRepresentation() throws ParseException {
        ResponseEntity<UserRepresentationModel> userRepresentation =
                userService.getUserRepresentationModelByUsername("Kimiko");

        Assert.assertEquals(HttpStatus.OK, userRepresentation.getStatusCode());
        if(userRepresentation.getBody() != null) {
            Assert.assertEquals(getTestUserKimiko(), userRepresentation.getBody().getUser());
        }
    }

    private User getTestUserKimiko() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = formatter.parse("1961-08-06");
        return new User()
                .setUsername("Kimiko")
                .setBirthDate(birthDate)
                .setCountryOfResidence("Japan")
                .setPhoneNumber("0633945969")
                .setGender(Gender.FEMALE);
    }

    @Test
    public void givenUnknownUser_whenQueried_thenThrowsException(){
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Cannot find an user with the specified username : Unknown");

        userService.getUserRepresentationModelByUsername("Unknown");
    }

    @Test
    public void givenValidRegistrationBean_whenRegistering_thenReturnCreatedUserRepresentation() throws ParseException {
        UserCreationBean creationBean = getValidUserCreationBean();
        ResponseEntity<UserRepresentationModel> createdUserRepresentation =
                userService.registerUserAndGetAsRepresentationModel(creationBean);

        Assert.assertEquals(HttpStatus.CREATED, createdUserRepresentation.getStatusCode());
        if(createdUserRepresentation.getBody() != null) {
            Assert.assertEquals(getTestUserJean(), createdUserRepresentation.getBody().getUser());
        }
    }

    @Test
    public void givenInvalidBirthDate_whenRegistering_thenThrows() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unparseable date: \"birthDate\"");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setBirthDate("birthDate");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    @Test
    public void givenFutureBirthDate_whenRegistering_thenThrows() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The birth date should be in the past");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setBirthDate("2042-08-05");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    @Test
    public void givenBadGender_whenRegistering_thenThrows() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No enum constant com.test.technical.model.Gender.INVALID_GENDER");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setGender("INVALID_GENDER");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    @Test
    public void givenNonLivingInFranceUser_whenRegistering_thenThrows() {
        expectedException.expect(InvalidUserException.class);
        expectedException.expectMessage("The given user is either not living in France and/or not an adult");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setCountryOfResidence("Portugal");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    @Test
    public void givenNonNotAdultUser_whenRegistering_thenThrows() {
        expectedException.expect(InvalidUserException.class);
        expectedException.expectMessage("The given user is either not living in France and/or not an adult");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setBirthDate("2010-08-05");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    @Test
    public void givenNonNotAdultUserAndNotLivingInFrance_whenRegistering_thenThrows() {
        expectedException.expect(InvalidUserException.class);
        expectedException.expectMessage("The given user is either not living in France and/or not an adult");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setCountryOfResidence("Portugal");
        creationBean.setBirthDate("2010-08-05");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    @Test
    public void givenAlreadyExistingUser_whenRegistering_thenThrows() {
        expectedException.expect(UserAlreadyExistsException.class);
        expectedException.expectMessage("An user already exists for this username : Martin");

        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setUsername("Martin");
        userService.registerUserAndGetAsRepresentationModel(creationBean);
    }

    private UserCreationBean getValidUserCreationBean() {
        return new UserCreationBean()
                .setUsername("Jean")
                .setBirthDate("1961-08-05")
                .setCountryOfResidence("France")
                .setPhoneNumber("0633945969")
                .setGender("MALE");
    }

    private User getTestUserJean() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = formatter.parse("1961-08-05");
        return new User()
                .setUsername("Jean")
                .setBirthDate(birthDate)
                .setCountryOfResidence("France")
                .setPhoneNumber("0633945969")
                .setGender(Gender.MALE);
    }


}
