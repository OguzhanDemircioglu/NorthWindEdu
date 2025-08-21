package com.server.app.repository;

import com.server.app.model.CustomerDemo;
import com.server.app.model.embedded.CustomerDemoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDemoRepository extends JpaRepository<CustomerDemo, CustomerDemoId> {

    Optional<CustomerDemo> findCustomerCustomerDemoByCustomerDemoId(CustomerDemoId id);

    void deleteCustomerDemoByCustomerDemoId(CustomerDemoId id);

    boolean existsByCustomerDemoId_CustomerIdAndCustomerDemoId_CustomerTypeId(String customerId, String customerTypeId);
}
