package com.todo.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String name;

    private String email;

    private String password;

    @Column(name = "accountEnabled")
    private Boolean accountEnabled;

    @Column(name = "accountLocked")
    private Boolean accountLocked;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<ProjectEntity> projects;

}
