package com.test.technical.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.technical.model.Gender;
import com.test.technical.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserRepresentationBean {

    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String countryOfResidence;
    private String phoneNumber;
    private Gender gender;

    public static UserRepresentationBean fromModel(User user) {
        UserRepresentationBean bean = new UserRepresentationBean();
        bean.username = user.getUsername();
        bean.birthDate = user.getBirthDate();
        bean.countryOfResidence = user.getCountryOfResidence();
        bean.phoneNumber = user.getPhoneNumber();
        bean.gender = user.getGender();
        return bean;
    }

}
