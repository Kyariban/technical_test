package com.test.technical.validation;

import com.test.technical.dto.UserCreationBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
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
        return UserCreationBean.builder()
                .username("Jean")
                .birthDate(LocalDate.parse("1961-08-05"))
                .countryOfResidence("France")
                .phoneNumber("0644649021")
                .gender("MALE")
                .build();
    }

    private Set<ConstraintViolation<UserCreationBean>> performValidation(UserCreationBean creationBean) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(creationBean);
    }

    private void assertValidationMessage(Set<ConstraintViolation<UserCreationBean>> constraintViolations,
                                         String expectedMessage) {
        assertEquals(1, constraintViolations.size());
        boolean doesMessageMatch = constraintViolations
                .stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(expectedMessage::equals);

        assertTrue(doesMessageMatch);
    }
}
