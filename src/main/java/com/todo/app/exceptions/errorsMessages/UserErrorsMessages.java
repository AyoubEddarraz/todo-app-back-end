package com.todo.app.exceptions.errorsMessages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorsMessages {


    USER_NOT_FOUND("user not found"),
    USER_ALREADY_EXSISTE("user already exist"),
    USER_IS_NOT_ENABLED("this is user is not enabled, please confirm your email."),
    USER_IS_LOCKED("this is user is locked, please contact support team to resolve the problem."),
    USER_PASSWORD_INCORRECT("password incorrect"),

    DATA_NOT_FOUND("data not found!"),
    ACCESS_DENIED_TO_THIS_RESOURCE("you don't have access to this resource!");

    private String errorMessage;

}
