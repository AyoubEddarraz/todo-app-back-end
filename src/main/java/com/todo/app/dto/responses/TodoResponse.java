package com.todo.app.dto.responses;

import lombok.Data;

@Data
public class TodoResponse {

    private String todoId;

    private String title;

    private String description;

    private String status;

}
