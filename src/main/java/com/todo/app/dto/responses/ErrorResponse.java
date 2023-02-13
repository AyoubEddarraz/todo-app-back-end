package com.todo.app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private Date timestamp;
    private String message;
    private int status;

}
