package com.user.registration.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class RegistrationRequest {
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;

}
