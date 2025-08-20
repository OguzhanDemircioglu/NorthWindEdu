package com.server.app.repository;

import com.server.app.model.CustomerCustomerDemo;
import com.server.app.model.embedded.CustomerCustomerDemoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerCustomerDemoRepository extends JpaRepository<CustomerCustomerDemo, CustomerCustomerDemoId> {

    Optional<CustomerCustomerDemo> findCustomerCustomerDemoByCustomerCustomerDemoId(CustomerCustomerDemoId id);

    void deleteCustomerCustomerDemoByCustomerCustomerDemoId(CustomerCustomerDemoId id);

    boolean existsByCustomerCustomerDemoId_CustomerIdAndCustomerCustomerDemoId_CustomerTypeId(String customerId, String customerTypeId);
}
