package com.server.app.model;

import com.server.app.model.embedded.CustomerCustomerDemoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_customer_demo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCustomerDemo {

    @EmbeddedId
    private CustomerCustomerDemoId customerCustomerDemoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerTypeId")
    @JoinColumn(name = "customer_type_id")
    private CustomerDemographics customerDemographics;
}
