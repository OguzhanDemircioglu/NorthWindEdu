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
    @Column(name = "region_id", unique = true, nullable = false, updatable = false)
    private Long regionId;

    @Column(name = "region_description", columnDefinition = "bpchar", nullable = false)
    private String regionDescription;
}
