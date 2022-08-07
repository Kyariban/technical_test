package com.test.technical.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@SuppressWarnings("unused")
public class UserCreationBean {

    @NotEmpty(message = "The username is mandatory")
    private String username;

    @NotEmpty(message = "The birth date is mandatory")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",
            message = "The date should be formatted as such : yyyy-MM-dd")
    private String birthDate;

    @NotEmpty(message = "The country or residence is mandatory")
    @Pattern(regexp = "[a-zA-Z]+", message = "The country of residence should only contain letters")
    private String countryOfResidence;

    @Pattern(regexp = "[0-9]+", message = "The phone number should only contain numeric characters")
    private String phoneNumber;

    private String gender;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserCreationBean{" +
                "username='" + username + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", countryOfResidence='" + countryOfResidence + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
