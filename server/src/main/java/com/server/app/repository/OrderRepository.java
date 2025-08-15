package com.server.app.repository;

import com.server.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Short> {

    Optional<Order> findOrderByOrderId(Short orderId);

    void deleteOrderByOrderId(Short orderId);

    boolean existsByCustomer_CustomerId(String customerId);
}
