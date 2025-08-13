package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employees") // tablo adı çoğul kalabilir, problem değil
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "last_name", length = 20, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 10, nullable = false)
    private String firstName;

    @Column(name = "title", length = 30)
    private String title;

    @Column(name = "title_of_courtesy", length = 25)
    private String titleOfCourtesy;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

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

    @Column(name = "home_phone", length = 24)
    private String homePhone;

    @Column(name = "extension", length = 4)
    private String extension;

    @Column(name = "photo", columnDefinition = "TEXT")
    private String photo;

    @Column(name = "notes")
    private String notes;

    @Column(name = "reports_to")
    private Integer reportsTo;

    @Column(name = "photo_path", length = 255)
    private String photoPath;
}
