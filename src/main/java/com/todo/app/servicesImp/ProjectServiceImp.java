package com.todo.app.servicesImp;

import com.todo.app.dto.requests.ProjectRequest;
import com.todo.app.dto.responses.ProjectResponse;
import com.todo.app.entities.ProjectEntity;
import com.todo.app.entities.UserEntity;
import com.todo.app.exceptions.errorsExceptions.NotFoundException;
import com.todo.app.exceptions.errorsExceptions.UserAccessDeniedException;
import com.todo.app.exceptions.errorsMessages.UserErrorsMessages;
import com.todo.app.repositories.ProjectRepository;
import com.todo.app.repositories.UserRepository;
import com.todo.app.services.ProjectService;
import com.todo.app.utils.GeneratorUtil;
import com.todo.app.utils.PrincipalUserUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImp implements ProjectService {

    private final ProjectRepository projectRepository;

    private final PrincipalUserUtil principalUserUtil = new PrincipalUserUtil();

    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    @Override
    public ProjectResponse getProjectById(String projectId) {

        String currentUser = principalUserUtil.getPrincipalUserName();

        UserEntity user = userRepository.findByEmail(currentUser);

        ProjectEntity project = projectRepository.findByProjectId(projectId);

        if (project == null) throw new NotFoundException(UserErrorsMessages.DATA_NOT_FOUND.getErrorMessage());

        boolean matched = project.getCreatedBy().getEmail().equals(user.getEmail());

        if (!matched) {
            throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());
        }

        return modelMapper.map(project, ProjectResponse.class);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {

        String currentUser = principalUserUtil.getPrincipalUserName();

        List<ProjectEntity> projects = projectRepository.findAllByCreatedBy_Email(currentUser);

        List<ProjectResponse> projectResponseList = Arrays.asList(modelMapper.map(projects, ProjectResponse[].class));

        return projectResponseList;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {

        String currentUser = principalUserUtil.getPrincipalUserName();
        UserEntity user = userRepository.findByEmail(currentUser);

        ProjectEntity project = modelMapper.map(projectRequest, ProjectEntity.class);

        project.setProjectId(generatorUtil.generateUID(32));
        project.setCreatedBy(user);

        ProjectEntity projectCreated = projectRepository.save(project);
        ProjectResponse projectResponse = modelMapper.map(projectCreated, ProjectResponse.class);

        return projectResponse;
    }

    @Override
    public ProjectResponse updateProject(ProjectRequest projectRequest, String projectId) {

        String currentUser = principalUserUtil.getPrincipalUserName();
        UserEntity user = userRepository.findByEmail(currentUser);

        ProjectEntity project = projectRepository.findByProjectId(projectId);

        if (project != null){
            boolean matched = project.getCreatedBy().getEmail().equals(user.getEmail());

            if (!matched) {
                throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());
            }

            project.setName(projectRequest.getName());
            project.setDescription(projectRequest.getDescription());

            ProjectEntity updatedProject = projectRepository.save(project);
            ProjectResponse projectResponse = modelMapper.map(updatedProject, ProjectResponse.class);

            return projectResponse;

        }else {
            throw new NotFoundException(UserErrorsMessages.DATA_NOT_FOUND.getErrorMessage());
        }

    }

    @Override
    public String deleteProject(String projectId) {

        String currentUser = principalUserUtil.getPrincipalUserName();
        UserEntity user = userRepository.findByEmail(currentUser);

        ProjectEntity project = projectRepository.findByProjectId(projectId);

        boolean matched = project.getCreatedBy().getEmail().equals(user.getEmail());

        if (!matched) {
            throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());
        }

        projectRepository.deleteByProjectId(projectId);

        return "project deleted successfully";
    }
}
