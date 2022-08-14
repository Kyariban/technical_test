package com.test.technical.validation;

import com.test.technical.dto.UserCreationBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserCreationBeanValidationTest {

    @Test
    public void givenNullUsername_whenValidating_shouldHaveErrors() {
        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setUsername(null);

        Set<ConstraintViolation<UserCreationBean>> constraintViolations = performValidation(creationBean);
        assertValidationMessage(constraintViolations, "The username is mandatory");
    }

    @Test
    public void givenNullBirthdate_whenValidating_shouldHaveErrors() {
        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setBirthDate(null);

        Set<ConstraintViolation<UserCreationBean>> constraintViolations = performValidation(creationBean);
        assertValidationMessage(constraintViolations, "The birth date is mandatory");
    }

    @Test
    public void givenInvalidBirthdate_whenValidating_shouldHaveErrors() {
        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setBirthDate("invalid");

        Set<ConstraintViolation<UserCreationBean>> constraintViolations = performValidation(creationBean);
        assertValidationMessage(constraintViolations, "The date should be formatted as such : yyyy-MM-dd");
    }

    @Test
    public void givenNullCountryOfResidence_whenValidating_shouldHaveErrors() {
        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setCountryOfResidence(null);

        Set<ConstraintViolation<UserCreationBean>> constraintViolations = performValidation(creationBean);
        assertValidationMessage(constraintViolations, "The country or residence is mandatory");
    }

    @Test
    public void givenInvalidCountryOfResidence_whenValidating_shouldHaveErrors() {
        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setCountryOfResidence("France123");

        Set<ConstraintViolation<UserCreationBean>> constraintViolations = performValidation(creationBean);
        assertValidationMessage(constraintViolations, "The country of residence should only contain letters");
    }

    @Test
    public void givenInvalidPhoneNumber_whenValidating_shouldHaveErrors() {
        UserCreationBean creationBean = getValidUserCreationBean();
        creationBean.setPhoneNumber("06VingtQuatre");

        Set<ConstraintViolation<UserCreationBean>> constraintViolations = performValidation(creationBean);
        assertValidationMessage(constraintViolations, "The phone number should only contain numeric characters");
    }

    private UserCreationBean getValidUserCreationBean() {
        return new UserCreationBean()
                .setUsername("Jean")
                .setBirthDate("1961-08-05")
                .setCountryOfResidence("France")
                .setPhoneNumber("0644649021")
                .setGender("MALE");
    }

    private Set<ConstraintViolation<UserCreationBean>> performValidation(UserCreationBean creationBean) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(creationBean);
    }

    private void assertValidationMessage(Set<ConstraintViolation<UserCreationBean>> constraintViolations,
                                         String expectedMessage) {
        Assert.assertEquals(1, constraintViolations.size());
        boolean doesMessageMatch = constraintViolations
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(expectedMessage::equals);

        Assert.assertTrue(doesMessageMatch);
    }
}
