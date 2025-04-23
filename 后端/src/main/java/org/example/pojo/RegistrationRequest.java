package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationRequest {
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private String verificationCode;
}