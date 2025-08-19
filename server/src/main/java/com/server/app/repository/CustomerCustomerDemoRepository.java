package com.server.app.repository;

import com.server.app.model.CustomerCustomerDemo;
import com.server.app.model.CcdId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerCustomerDemoRepository extends JpaRepository<CustomerCustomerDemo, CcdId> {

    Optional<CustomerCustomerDemo> findCustomerCustomerDemoByCcdId(CcdId id);

    void deleteCustomerCustomerDemoByCcdId(CcdId id);

    boolean existsByCcdId_CustomerIdAndCcdId_CustomerTrypeId(String customerId, String customerTypeId);
}
