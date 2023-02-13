package com.todo.app.dto.responses;

import com.todo.app.entities.TodoEntity;
import com.todo.app.entities.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProjectResponse {

    private String projectId;
    private String name;
    private String description;
    private UserResponse createdBy;
    private List<TodoResponse> todos;

}
