package com.server.app.repository;

import com.server.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findOrderByOrderId(Long id);

    void deleteOrderByOrderId(Long id);

    boolean existsOrderByOrderId(Long id);

    Order getOrderByOrderId(Long id);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE order_seq RESTART WITH 1", nativeQuery = true)
    void resetOrderSequence();
}
