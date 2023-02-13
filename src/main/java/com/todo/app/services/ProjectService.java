package com.todo.app.services;

import com.todo.app.dto.requests.ProjectRequest;
import com.todo.app.dto.responses.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse getProjectById(String projectId);

    List<ProjectResponse> getAllProjects();

    ProjectResponse createProject(ProjectRequest projectRequest);

    ProjectResponse updateProject(ProjectRequest projectRequest, String projectId);

    String deleteProject(String projectId);

}
