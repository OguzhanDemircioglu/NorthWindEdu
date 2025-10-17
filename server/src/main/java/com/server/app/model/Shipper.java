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
    @Column(name = "shipper_id", nullable = false)
    private Long shipperId;

    @Column(name = "company_name", length = 40, nullable = false)
    private String companyName;

    @Column(name = "phone", length = 24)
    private String phone;
}
