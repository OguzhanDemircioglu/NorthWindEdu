package com.server.app.model;

import com.server.app.model.embedded.CustomerDemoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_demos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDemo {

    @EmbeddedId
    private CustomerDemoId customerDemoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerTypeId")
    @JoinColumn(name = "customer_type_id")
    private CustomerDemographics customerDemographics;
}
