package com.test.technical.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.model.Gender;
import com.test.technical.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserTestParent {
    protected User getTestUserJean() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = formatter.parse("1961-08-05");
        return new User()
                .setUsername("Jean")
                .setBirthDate(birthDate)
                .setCountryOfResidence("France")
                .setPhoneNumber("0633945969")
                .setGender(Gender.MALE);
    }

    protected User getTestUserKimiko() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = formatter.parse("1961-08-06");
        return new User()
                .setUsername("Kimiko")
                .setBirthDate(birthDate)
                .setCountryOfResidence("Japan")
                .setPhoneNumber("0633945969")
                .setGender(Gender.FEMALE);
    }

    protected UserCreationBean getValidUserCreationBean() {
        return new UserCreationBean()
                .setUsername("Jean")
                .setBirthDate("1961-08-05")
                .setCountryOfResidence("France")
                .setPhoneNumber("0633945969")
                .setGender("MALE");
    }

    protected String asJsonString(UserCreationBean creationBean) {
        try {
            return new ObjectMapper().writeValueAsString(creationBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
