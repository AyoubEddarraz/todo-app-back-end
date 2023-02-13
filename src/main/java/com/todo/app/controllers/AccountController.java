package com.todo.app.controllers;

import com.todo.app.dto.requests.RegisterRequest;
import com.todo.app.dto.responses.TokenResponse;
import com.todo.app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(
            path = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest userRegisterRequest) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.register(userRegisterRequest));
    }

    @PostMapping(
            path = "/refresh",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.refreshToken(request, response));
    }

    @PostMapping(
            path = "/confirm",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> confirmAccount(@RequestParam(name = "token") String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.confirmAccount(token));
    }

    @PostMapping(
            path = "/sendConfirmationMail",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> sendConfirmationMailAfterRegistration(@RequestParam(name = "email") String email) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.sendConfirmationMailAfterRegistration(email));
    }

}
