package com.todo.app.controllers;

import com.todo.app.dto.requests.TodoRequest;
import com.todo.app.dto.responses.TodoResponse;
import com.todo.app.services.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping(
            path = "/{todoId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TodoResponse> getTodo(@PathVariable String todoId) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId));
    }

    @GetMapping(
            path = "/all/{projectId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<List<TodoResponse>> getAllTodos(@PathVariable String projectId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.getAllTodos(projectId));
    }


    @PostMapping(
            path = "/{projectId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest todoRequest, @PathVariable String projectId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todoRequest, projectId));
    }


    @PutMapping(
            path = "/{todoId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TodoResponse> updateProject(@RequestBody TodoRequest todoRequest, @PathVariable String todoId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(todoService.updateTodo(todoRequest, todoId));
    }


    @DeleteMapping(path = "/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable String todoId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(todoService.deleteTodo(todoId));
    }

}
