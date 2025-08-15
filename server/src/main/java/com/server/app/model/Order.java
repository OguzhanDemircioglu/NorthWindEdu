package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @Column(name = "order_id", nullable = false)
    private Short orderId; // smallint → Short

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_via")
    private Shipper shipVia;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "required_date")
    private LocalDate requiredDate;

    @Column(name = "shipped_date")
    private LocalDate shippedDate;

    @Column(name = "freight")
    private Float freight;

    @Column(name = "ship_name", length = 40)
    private String shipName;

    @Column(name = "ship_address", length = 60)
    private String shipAddress;

    @Column(name = "ship_city", length = 15)
    private String shipCity;

    @Column(name = "ship_region", length = 15)
    private String shipRegion;

    @Column(name = "ship_postal_code", length = 10)
    private String shipPostalCode;

    @Column(name = "ship_country", length = 15)
    private String shipCountry;
}
