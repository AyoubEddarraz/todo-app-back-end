package com.todo.app.servicesImp;

import com.todo.app.dto.requests.TodoRequest;
import com.todo.app.dto.responses.ProjectResponse;
import com.todo.app.dto.responses.TodoResponse;
import com.todo.app.entities.ProjectEntity;
import com.todo.app.entities.TodoEntity;
import com.todo.app.entities.UserEntity;
import com.todo.app.exceptions.errorsExceptions.NotFoundException;
import com.todo.app.exceptions.errorsExceptions.UserAccessDeniedException;
import com.todo.app.exceptions.errorsMessages.UserErrorsMessages;
import com.todo.app.repositories.ProjectRepository;
import com.todo.app.repositories.TodoRepository;
import com.todo.app.repositories.UserRepository;
import com.todo.app.services.TodoService;
import com.todo.app.utils.GeneratorUtil;
import com.todo.app.utils.PrincipalUserUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImp implements TodoService {

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PrincipalUserUtil principalUserUtil = new PrincipalUserUtil();

    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private final ProjectRepository projectRepository;

    @Override
    public TodoResponse getTodoById(String todoId) {

        String currentUser = principalUserUtil.getPrincipalUserName();

        UserEntity user = userRepository.findByEmail(currentUser);

        TodoEntity todo = todoRepository.findByTodoId(todoId);

        if (todo != null){
            boolean matched = todo.getProject().getCreatedBy().getEmail().equals(user.getEmail());

            if (!matched) {
                throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());
            }

            return modelMapper.map(todo, TodoResponse.class);
        }else {
            throw new NotFoundException(UserErrorsMessages.DATA_NOT_FOUND.getErrorMessage());
        }

    }

    @Override
    public List<TodoResponse> getAllTodos(String projectId) {

        List<TodoEntity> todos = todoRepository.findAllByProject_ProjectId(projectId);

        List<TodoResponse> todosResponsesList = Arrays.asList(modelMapper.map(todos, TodoResponse[].class));

        return todosResponsesList;

    }

    @Override
    public TodoResponse createTodo(TodoRequest todoRequest, String projectId) {

        String currentUser = principalUserUtil.getPrincipalUserName();
        UserEntity user = userRepository.findByEmail(currentUser);

        TodoEntity todo = modelMapper.map(todoRequest, TodoEntity.class);

        ProjectEntity project = projectRepository.findByProjectId(projectId);

        if (project == null) throw new NotFoundException(UserErrorsMessages.DATA_NOT_FOUND.getErrorMessage());

        boolean matched = project.getCreatedBy().getEmail().equals(user.getEmail());

        if (!matched) throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());

        todo.setTodoId(generatorUtil.generateUID(32));
        todo.setProject(project);

        TodoEntity todoCreated = todoRepository.save(todo);
        TodoResponse todoResponse = modelMapper.map(todoCreated, TodoResponse.class);

        return todoResponse;

    }

    @Override
    public TodoResponse updateTodo(TodoRequest todoRequest, String todoId) {

        String currentUser = principalUserUtil.getPrincipalUserName();
        UserEntity user = userRepository.findByEmail(currentUser);

        TodoEntity todo = todoRepository.findByTodoId(todoId);

        if (todo != null){

            boolean matched = todo.getProject().getCreatedBy().getEmail().equals(user.getEmail());

            if (!matched) {
                throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());
            }

            todo.setTitle(todoRequest.getTitle());
            todo.setDescription(todoRequest.getDescription());
            todo.setStatus(todoRequest.getStatus());

            TodoEntity updatedTodo = todoRepository.save(todo);
            TodoResponse todoResponse = modelMapper.map(updatedTodo, TodoResponse.class);

            return todoResponse;

        }else {
            throw new NotFoundException(UserErrorsMessages.DATA_NOT_FOUND.getErrorMessage());
        }

    }

    @Override
    public String deleteTodo(String todoId) {

        String currentUser = principalUserUtil.getPrincipalUserName();
        UserEntity user = userRepository.findByEmail(currentUser);

        TodoEntity todo = todoRepository.findByTodoId(todoId);

        boolean matched = todo.getProject().getCreatedBy().getEmail().equals(user.getEmail());

        if (!matched) {
            throw new UserAccessDeniedException(UserErrorsMessages.ACCESS_DENIED_TO_THIS_RESOURCE.getErrorMessage());
        }

        todoRepository.deleteByTodoId(todoId);

        return "todo deleted successfully";

    }
}
