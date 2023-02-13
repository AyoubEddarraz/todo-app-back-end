package com.todo.app.exceptions.errorsExceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String errorMessage){
        super(errorMessage);
    }

}
