package com.server.app.repository;

import com.server.app.model.CustomerDemographics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDemographicsRepository extends JpaRepository<CustomerDemographics, String> {
    Optional<CustomerDemographics> findCustomerDemographicsByCustomerTypeId(String customerTypeId);

    void deleteCustomerDemographicsByCustomerTypeId(String customerTypeId);
}
