package org.example.service;

import org.example.pojo.User;

public interface RegistrationService {
    void sendEmail(String email, String code);

    void add(User user);
}
