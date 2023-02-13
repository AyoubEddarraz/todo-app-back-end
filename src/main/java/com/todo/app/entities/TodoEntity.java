package com.todo.app.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "todo")
@Data
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String todoId;

    private String title;

    private String description;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

}
