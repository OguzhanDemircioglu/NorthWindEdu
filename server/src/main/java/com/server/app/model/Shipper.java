package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shippers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipper {

    @Id
    @SequenceGenerator(name = "shipper_seq", sequenceName = "shipper_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipper_seq")
    @Column(name = "shipper_id", nullable = false)
    private Short shipperId; // smallint → Short

    @Column(name = "company_name", length = 40, nullable = false)
    private String companyName;

    @Column(name = "phone", length = 24)
    private String phone;
}
