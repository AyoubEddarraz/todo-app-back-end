package com.todo.app.controllers;

import com.todo.app.dto.requests.ProjectRequest;
import com.todo.app.dto.responses.ProjectResponse;
import com.todo.app.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(
            path = "/{projectId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectById(projectId));
    }


    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectRequest));
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.getAllProjects());
    }


    @PutMapping(
            path = "/{projectId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody ProjectRequest projectRequest, @PathVariable String projectId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(projectService.updateProject(projectRequest, projectId));
    }


    @DeleteMapping(path = "/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(projectService.deleteProject(projectId));
    }



}
