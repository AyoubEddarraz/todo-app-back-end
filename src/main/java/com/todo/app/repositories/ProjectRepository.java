package com.todo.app.repositories;

import com.todo.app.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    ProjectEntity findByProjectId(String projectId);

    List<ProjectEntity> findAllByCreatedBy_Email(String email);

    void deleteByProjectId(String projectId);

}
