package com.todo.app.exceptions.errorsExceptions;

public class UserException extends RuntimeException{

    public UserException(String errorMessage){
        super(errorMessage);
    }

}
