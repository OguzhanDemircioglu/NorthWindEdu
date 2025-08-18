package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @SequenceGenerator(name = "supplier_seq", sequenceName = "supplier_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "company_name", length = 40, nullable = false)
    private String companyName;

    @Column(name = "contact_name", length = 30)
    private String contactName;

    @Column(name = "contact_title", length = 30)
    private String contactTitle;

    @Column(name = "address", length = 60)
    private String address;

    @Column(name = "city", length = 15)
    private String city;

    @Column(name = "region", length = 15)
    private String region;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "country", length = 15)
    private String country;

    @Column(name = "phone", length = 24)
    private String phone;
    @Column(name = "fax", length = 24)

    private String fax;

    @Column(name = "homepage", columnDefinition = "TEXT")
    private String homepage;
}
