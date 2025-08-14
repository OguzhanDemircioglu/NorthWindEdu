package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @SequenceGenerator(name = "region_seq", sequenceName = "region_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_seq")
    @Column(name = "region_id", unique = true, nullable = false, updatable = false)
    private Short regionId;

    @Column(name = "region_description", columnDefinition = "bpchar", nullable = false)
    private String regionDescription;
}
