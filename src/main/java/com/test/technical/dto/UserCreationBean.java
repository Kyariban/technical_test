package com.test.technical.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@EqualsAndHashCode
public class UserCreationBean {

    @NotEmpty(message = "The username is mandatory")
    private String username;

    @NotNull(message = "The birth date is mandatory")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Allow leniency
    @Past(message = "The birth date must be in the past")
    private LocalDate birthDate;

    @NotEmpty(message = "The country or residence is mandatory")
    @Pattern(regexp = "[a-zA-Z]+", message = "The country of residence should only contain letters")
    private String countryOfResidence;

    @Pattern(regexp = "[0-9]+", message = "The phone number should only contain numeric characters")
    private String phoneNumber;

    private String gender;
}
