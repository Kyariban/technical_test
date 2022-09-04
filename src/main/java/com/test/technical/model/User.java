package com.test.technical.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.technical.dto.UserCreationBean;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;



@Entity
@Table(name = "USERS")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
public class User {

    @Id
    private String username;
    private LocalDate birthDate;
    private String countryOfResidence;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;


    @JsonIgnore
    public Boolean isValidForRegistration() {
        return "France".equals(this.countryOfResidence) && this.getCurrentAge() >= 18;
    }

    private int getCurrentAge() {
        if (birthDate != null) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        } else {
            throw new IllegalArgumentException("Cannot retrieve the age of the user, missing birth date");
        }
    }

    public static User fromCreationBean(UserCreationBean bean) {
        User user =  User.builder()
                .username(bean.getUsername())
                .birthDate(bean.getBirthDate())
                .countryOfResidence(bean.getCountryOfResidence())
                .phoneNumber(bean.getPhoneNumber())
                .build();
        if(bean.getGender() != null) {
            user.setGender(Gender.valueOf(bean.getGender()));
        }
        return user;
    }
}