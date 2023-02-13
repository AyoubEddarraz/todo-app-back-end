package com.todo.app.dto.requests;

import com.todo.app.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    @Size(max = 100)
    private String name;

    @NotBlank(message = ValidationConstants.NOT_BLANK)
    @Email(message = ValidationConstants.EMAIL)
    private String email;

    @NotBlank
    @Size(min = 8, message = ValidationConstants.MIN_SIZE_8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = ValidationConstants.PASSWORD)
    private String password;

}
