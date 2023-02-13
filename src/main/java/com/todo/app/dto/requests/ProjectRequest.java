package com.todo.app.dto.requests;

import com.todo.app.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ProjectRequest {

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    @Size(max = 100)
    private String name;

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    @Size(max = 250)
    private String description;

}
