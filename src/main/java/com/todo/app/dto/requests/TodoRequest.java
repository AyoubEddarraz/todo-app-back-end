package com.todo.app.dto.requests;

import com.todo.app.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class TodoRequest {

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    @Size(max = 100)
    private String title;

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    @Size(max = 250)
    private String description;

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    private String status;

}
