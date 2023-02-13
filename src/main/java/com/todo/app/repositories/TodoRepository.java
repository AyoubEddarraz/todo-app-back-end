package com.todo.app.repositories;

import com.todo.app.entities.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    TodoEntity findByTodoId(String todoId);

    List<TodoEntity> findAllByProject_ProjectId(String projectId);

    void deleteByTodoId(String todoId);

}
