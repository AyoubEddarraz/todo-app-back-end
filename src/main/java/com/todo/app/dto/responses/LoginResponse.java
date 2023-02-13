package com.todo.app.dto.responses;

import lombok.Data;

@Data
public class LoginResponse {

    private String userId;
    private String token;
    private String refreshToken;

}
