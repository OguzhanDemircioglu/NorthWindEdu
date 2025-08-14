package com.server.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_demographics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDemographics {

    @Id
    @Column(name = "customer_type_id", length = 1, nullable = false) // bpchar => fixed-length char
    private String customerTypeId;

    @Column(name = "customer_desc", columnDefinition = "TEXT")
    private String customerDesc;
}
