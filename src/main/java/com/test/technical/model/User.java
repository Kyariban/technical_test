package com.test.technical.model;

import com.test.technical.dto.UserCreationBean;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@SuppressWarnings({"unused", "UnusedReturnValue"})
@Entity
@Table(name = "USERS")
public class User {

    @Id
    private String username;
    private Date birthDate;
    private String countryOfResidence;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public User setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public User setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public User setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Boolean isValidForRegistration() {
        return "France".equals(this.countryOfResidence) && this.getCurrentAge() >= 18;
    }

    private int getCurrentAge() {
        if (birthDate != null) {
            LocalDate birthLocaleDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthLocaleDate, LocalDate.now()).getYears();
        } else {
            throw new IllegalArgumentException("Cannot retrieve the age of the user, missing birth date");
        }
    }

    public static User fromCreationBean(UserCreationBean bean) throws ParseException {
        Date birthDate = parseAndValidateBirthDate(bean);
        User user = new User()
                .setUsername(bean.getUsername())
                .setBirthDate(birthDate)
                .setCountryOfResidence(bean.getCountryOfResidence())
                .setPhoneNumber(bean.getPhoneNumber());
        if(bean.getGender() != null) {
            user.setGender(Gender.valueOf(bean.getGender()));
        }
        return user;
    }

    private static Date parseAndValidateBirthDate(UserCreationBean bean) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = formatter.parse(bean.getBirthDate());
        if(!birthDate.before(new Date())) {
            throw new IllegalArgumentException("The birth date should be in the past");
        }
        return birthDate;
    }
}