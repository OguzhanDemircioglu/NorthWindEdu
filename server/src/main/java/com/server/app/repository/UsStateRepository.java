package com.server.app.repository;

import com.server.app.model.UsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsStateRepository extends JpaRepository<UsState, Long> {

    Optional<UsState> findStateByStateId(Long id);

    void deleteStateByStateId(Long id);

    boolean existsStateByStateId(Long id);

    @Query("SELECT MAX(us.stateId) FROM UsState us")
    Long findMaxId();
}
