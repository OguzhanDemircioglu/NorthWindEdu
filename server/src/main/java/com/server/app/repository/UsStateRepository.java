package com.server.app.repository;

import com.server.app.model.UsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsStateRepository extends JpaRepository<UsState, Long> {

    Optional<UsState> findStateByStateId(Long id);

    void deleteStateByStateId(Long id);

    boolean existsStateByStateId(Long id);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE state_seq RESTART WITH 1", nativeQuery = true)
    void resetStateSequence();
}
