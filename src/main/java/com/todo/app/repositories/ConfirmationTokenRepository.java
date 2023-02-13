package com.todo.app.repositories;

import com.todo.app.entities.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    @Query("SELECT ct FROM ConfirmationTokenEntity ct LEFT JOIN FETCH ct.user WHERE ct.token = :token")
    ConfirmationTokenEntity findByToken(@Param(value = "token") String token);

    @Modifying
    @Query(value = "delete FROM ConfirmationTokenEntity ct WHERE ct.token = :token")
    void deleteByToken(@Param(value = "token") String token);

    @Modifying
    @Query(value = "DELETE FROM ConfirmationTokenEntity ct WHERE ct.confirmed = false")
    void deleteExpiredToken();

}
