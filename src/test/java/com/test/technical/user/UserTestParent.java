package com.test.technical.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.model.Gender;
import com.test.technical.model.User;

import java.time.LocalDate;

public class UserTestParent {

    private final ObjectMapper objectMapper;

    public UserTestParent(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected User getTestUserJean() {
        LocalDate birthDate = LocalDate.parse("1961-08-05");
        return User.builder()
                .username("Jean")
                .birthDate(birthDate)
                .countryOfResidence("France")
                .phoneNumber("0644649021")
                .gender(Gender.MALE)
                .build();
    }

    protected User getTestUserKimiko() {
        LocalDate birthDate = LocalDate.parse("1961-08-06");
        return User.builder()
                .username("Kimiko")
                .birthDate(birthDate)
                .countryOfResidence("Japan")
                .phoneNumber("0644649021")
                .gender(Gender.FEMALE)
                .build();
    }

    protected UserCreationBean getValidUserCreationBean() {
        return UserCreationBean.builder()
                .username("Jean")
                .birthDate(LocalDate.parse("1961-08-05"))
                .countryOfResidence("France")
                .phoneNumber("0644649021")
                .gender("MALE")
                .build();
    }

    protected String asJsonString(UserCreationBean creationBean) {
        try {
            return objectMapper.writeValueAsString(creationBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
