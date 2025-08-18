package com.server.app.repository;

import com.server.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findOrderByOrderId(Long id);

    void deleteOrderByOrderId(Long id);

    boolean existsOrderByOrderId(Long id);
}
