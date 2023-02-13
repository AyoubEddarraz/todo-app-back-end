package com.todo.app.exceptions.errorsMessages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorsMessages {

    JWT_EXPIRED("token expired"),
    JWT_MISSING("token is missing"),
    JWT_EXPIRED_OR_NOT_VALID("Token is not valid or has expired.");

    private String errorMessage;

}
