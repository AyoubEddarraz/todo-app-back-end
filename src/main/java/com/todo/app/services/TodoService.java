package com.todo.app.services;

import com.todo.app.dto.requests.TodoRequest;
import com.todo.app.dto.responses.TodoResponse;

import java.util.List;

public interface TodoService {

    TodoResponse getTodoById(String todoId);

    List<TodoResponse> getAllTodos(String projectId);

    TodoResponse createTodo(TodoRequest todoRequest, String projectId);

    TodoResponse updateTodo(TodoRequest todoRequest, String todoId);

    String deleteTodo(String todoId);



}
