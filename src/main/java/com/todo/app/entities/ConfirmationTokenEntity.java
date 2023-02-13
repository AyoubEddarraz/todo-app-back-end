package com.todo.app.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "confirmation_tokens")
@Data
public class ConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "expiredAt")
    private Date expiredAt;

    @Column(name = "confirmedAt")
    private Date confirmedAt;

    @Column(name = "confirmed")
    private boolean confirmed;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
