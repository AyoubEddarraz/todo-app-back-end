package com.todo.app.exceptions.errorsExceptions;

public class UserAccessDeniedException extends RuntimeException{

    public UserAccessDeniedException(String errorMessage) {
        super(errorMessage);
    }

}
