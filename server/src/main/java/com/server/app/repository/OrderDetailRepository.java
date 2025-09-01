package com.server.app.repository;

import com.server.app.model.OrderDetail;
import com.server.app.model.embedded.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

    Optional<OrderDetail> findOrderDetailById(OrderDetailId id);

    void deleteOrderDetailById(OrderDetailId id);

    boolean existsOrderDetailById(OrderDetailId id);

    List<OrderDetail> getOrderDetailById_OrderId(Long orderId);

}
