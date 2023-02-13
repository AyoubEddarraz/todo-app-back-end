package com.todo.app.services;

import com.todo.app.dto.requests.RegisterRequest;
import com.todo.app.dto.responses.TokenResponse;
import com.todo.app.entities.UserEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountService {

    UserEntity getUserByEmail(String email);

    Object register(RegisterRequest userRegisterRequest) throws MessagingException;

    TokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    Object confirmAccountMailing(UserEntity user) throws MessagingException;

    Object sendConfirmationMailAfterRegistration(String email) throws MessagingException;

    Object confirmAccount(String token);

}
